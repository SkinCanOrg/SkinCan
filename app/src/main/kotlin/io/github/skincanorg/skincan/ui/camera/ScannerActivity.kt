/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.Database
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityScannerBinding
import io.github.skincanorg.skincan.lib.Util
import io.github.skincanorg.skincan.ui.result.ResultActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.*
import java.nio.channels.FileChannel
import javax.inject.Inject

@AndroidEntryPoint
class ScannerActivity : AppCompatActivity() {
    private var photoFile: File? = null
    private var shouldLoop = false
    private var looped = true
    private lateinit var image: Bitmap

    @Inject
    lateinit var database: Database

    private val binding: ActivityScannerBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            photoFile = intent.extras?.get("IMG_FILE") as File?
            if (photoFile != null) {
                image = Util.processBitmap(
                    BitmapFactory.decodeFile(photoFile!!.path),
                    photoFile!!,
                )

                Glide.with(ivImagePreview)
                    .load(image)
                    .into(ivImagePreview)
            } else {
                finish()
            }

            root.setTransitionListener(
                object : TransitionListener {
                    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                        if (shouldLoop) {
                            if (!looped)
                                motionLayout.transitionToState(R.id.loop_start)
                            else
                                motionLayout.transitionToState(R.id.loop_end)
                            looped = !looped
                        }
                    }

                    override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}

                    override fun onTransitionChange(
                        motionLayout: MotionLayout?,
                        startId: Int,
                        endId: Int,
                        progress: Float,
                    ) {
                    }

                    override fun onTransitionTrigger(
                        motionLayout: MotionLayout?,
                        triggerId: Int,
                        positive: Boolean,
                        progress: Float,
                    ) {
                    }

                },
            )

            btnScan.setOnClickListener {
                shouldLoop = true
                root.transitionToState(R.id.loop_start, 1)

                doPrediction(image)
            }

            btnCancel.setOnClickListener {
                finish()
            }
        }
    }

    private fun doPrediction(bitmap: Bitmap) {
        FirebaseModelDownloader.getInstance()
            .getModel(
                "CancerDetector",
                DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                CustomModelDownloadConditions.Builder().build(),
            )
            .addOnSuccessListener { model ->
                val modelFile = model?.file ?: return@addOnSuccessListener
                val interpreter = Interpreter(modelFile)

                val inputShape = interpreter.getInputTensor(0).shape()
                val inputSize = Size(inputShape[2], inputShape[1])

                val imgProcessor = ImageProcessor.Builder()
                    .add(
                        ResizeOp(
                            inputSize.height, inputSize.width, ResizeOp.ResizeMethod.BILINEAR,
                        ),
                    )
                    .add(NormalizeOp(0f, 1f))
                    .build()
                val tensorImage = imgProcessor.process(TensorImage(DataType.FLOAT32).apply { load(bitmap) })

                val modelOutput = TensorBuffer.createFixedSize(interpreter.getOutputTensor(0).shape(), DataType.FLOAT32)

                interpreter.run(tensorImage.buffer, modelOutput.buffer.rewind())

                val probProcessor = TensorProcessor.Builder()
                    .add(NormalizeOp(0f, 1f))
                    .build()

                val labels = TensorLabel(
                    BufferedReader(
                        InputStreamReader(assets.open("model_labels.txt")),
                    ).readLines(),
                    probProcessor.process(modelOutput),
                )

                val resultMap = labels.mapWithFloatValue

                var result = "Clear"
                var lastHighest = .50f
                resultMap.keys.forEach {
                    val value = resultMap[it] as Float
                    if (value >= lastHighest) {
                        if (value != lastHighest)
                            lastHighest = value
                        result = StringBuilder().apply {
                            append("$it ")
                            append(String.format("%.2f", value))
                        }.toString()
                    }
                    Log.d(
                        "ziML",
                        StringBuilder().apply {
                            append("$it ")
                            append(String.format("%.2f", value))
                        }.toString(),
                    )
                }

                Log.d("ziML", result)

                lifecycleScope.launch(Dispatchers.IO) {
                    delay(3000L)
                    val q = database.resultsQueries

                    val currentTime = System.currentTimeMillis() / 1000
                    val cacheDir = Util.getCacheDir(applicationContext, "results")

                    var outChan: FileChannel? = null
                    var inChan: FileChannel? = null
                    val newFile = File(cacheDir, currentTime.toString())
                    try {
                        outChan = FileOutputStream(newFile).channel
                        inChan = FileInputStream(photoFile).channel
                        inChan.transferTo(0, inChan.size(), outChan)
                        inChan.close()
                        photoFile!!.delete()
                    } finally {
                        inChan?.close()
                        outChan?.close()
                    }

                    val path = newFile.path
                    q.insert(path, result, currentTime)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ScannerActivity, "ML Result: $result", Toast.LENGTH_LONG).show()
                        startActivity(
                            Intent(this@ScannerActivity, ResultActivity::class.java).apply {
                                putExtra(ResultActivity.PHOTO_PATH, path)
                                putExtra(ResultActivity.RESULT, result)
                                putExtra(ResultActivity.TIMESTAMP, currentTime)
                                putExtra(ResultActivity.FROM, 0)
                            },
                        )
                        shouldLoop = false
                        finish()
                    }
                }
            }.addOnFailureListener { finish() }
    }
}
