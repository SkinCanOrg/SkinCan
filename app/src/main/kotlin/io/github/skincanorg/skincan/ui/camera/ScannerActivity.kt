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
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import org.tensorflow.lite.Interpreter
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScannerActivity : AppCompatActivity() {
    private val binding: ActivityScannerBinding by viewBinding(CreateMethod.INFLATE)
    private var shouldLoop = false
    private var looped = true
    private val imageSize = 28
    private var interpreter: Interpreter? = null
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

                val resizedImg = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(resizedImg)
            }

            btnCancel.setOnClickListener {
                finish()
            }
        }
    }

    private fun classifyImage(bitmap: Bitmap) {
        // TODO - Keeps giving me 6 (label: df)
        val numBytesPerChannel = 4 // float
        val input =
            ByteBuffer.allocateDirect(1 * imageSize * imageSize * 3 * numBytesPerChannel).order(ByteOrder.nativeOrder())
        for (y in 0 until imageSize) {
            for (x in 0 until imageSize) {
                val px = bitmap.getPixel(x, y)

                // Get channel values from the pixel value.
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                // Not sure, but this should be [0.0, 0.1]?
                val rf = r / 255f
                val gf = g / 255f
                val bf = b / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }
        val bufferSize = 1 * 7 * numBytesPerChannel
        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
        interpreter?.run(input, modelOutput)
        modelOutput.rewind()
        val probabilities = modelOutput.asFloatBuffer()
        for (i in 0 until probabilities.capacity()) {
            Log.d("ziML", i.toString())
        }
        shouldLoop = false
    }
}
