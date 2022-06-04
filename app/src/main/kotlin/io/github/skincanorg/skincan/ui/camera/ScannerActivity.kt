/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityScannerBinding
import io.github.skincanorg.skincan.lib.Util
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class ScannerActivity : AppCompatActivity() {
    private val binding: ActivityScannerBinding by viewBinding(CreateMethod.INFLATE)
    private var shouldLoop = false
    private var looped = true
    private val imageSize = 28
    private lateinit var interpreter: Interpreter
    private lateinit var image: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("CancerDetector", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model ->
                val modelFile = model?.file
                if (modelFile != null)
                    interpreter = Interpreter(modelFile)
            }

        binding.apply {
            val photoFile = intent.extras?.get("IMG_FILE") as File?
            if (photoFile != null) {
                image = Util.processBitmap(
                    BitmapFactory.decodeFile(photoFile.path),
                    photoFile,
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

        var result = "No cancer"
        resultMap.keys.forEach {
            val value = resultMap[it] as Float
            if (value >= .50f) {
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
        // TODO: Result page
        Toast.makeText(this@ScannerActivity, "ML Result: $result", Toast.LENGTH_LONG).show()
        shouldLoop = false
    }
}
