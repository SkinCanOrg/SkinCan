/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityCameraBinding
import io.github.skincanorg.skincan.lib.Extension.toFile
import io.github.skincanorg.skincan.lib.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private val binding: ActivityCameraBinding by viewBinding(CreateMethod.INFLATE)
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var isTorchOn = false
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: Camera

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted())
                finish()
        }
    }

    private fun goToScanner(file: File, isBackCamera: Boolean? = null) {
        lifecycleScope.launch(Dispatchers.Main) {
            // Making sure camera is unloaded
            cameraProvider.unbindAll()
        }

        val intent = Intent(this@CameraActivity, ScannerActivity::class.java)
        intent.putExtra("IMG_FILE", file)
        if (isBackCamera != null)
            intent.putExtra("IS_BACK_CAMERA", isBackCamera)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!allPermissionsGranted())
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS,
            )

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.apply {
            captureImg.setOnClickListener {
                val photoFile = Util.createFile(this@CameraActivity.application)

                val metadata = ImageCapture.Metadata().apply {
                    // Mirror image when using the front camera
                    isReversedHorizontal = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
                }

                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                    .setMetadata(metadata)
                    .build()

                imageCapture?.takePicture(
                    outputOptions, cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        private val TAG = "ziCameraX"

                        override fun onError(exc: ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            goToScanner(photoFile, cameraSelector != CameraSelector.DEFAULT_FRONT_CAMERA)
                        }
                    },
                )
            }

            closeCamera.setOnClickListener {
                finish()
            }

            flipCamera.setOnClickListener {
                cameraSelector =
                    if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }

            gallery.setOnClickListener {
                startGallery()
            }

            torchCamera.setOnClickListener {
                isTorchOn = !isTorchOn
                invalidateTorchState()
                camera.cameraControl.enableTorch(isTorchOn)
            }
        }
    }

    private fun invalidateTorchState() {
        binding.torchCamera.apply {
            when (isTorchOn) {
                true -> setImageResource(R.drawable.ic_flash)
                false -> setImageResource(R.drawable.ic_flash_off)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    //for start camera
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

                val screenAspectRatio = AspectRatio.RATIO_16_9

                val preview = Preview.Builder()
                    .setTargetAspectRatio(screenAspectRatio)
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder()
                    .setTargetAspectRatio(screenAspectRatio)
                    .build()

                cameraProvider.unbindAll()

                try {
                    camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture,
                    )
                    camera.cameraControl.enableTorch(isTorchOn)
                } catch (exc: Exception) {
                    Util.makeToastShort(this, getString(R.string.failed_open_camera))
                }
            },
            ContextCompat.getMainExecutor(this),
        )
    }

    // for open gallery
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val file = selectedImg.toFile(applicationContext)
            goToScanner(file)
        }
    }

    //PS : correct this if im wrong comrades :'v
    //here your bonus : 177013, 403509

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
