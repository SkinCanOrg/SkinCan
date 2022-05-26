/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.main.NewsAdapter
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import io.github.skincanorg.skincan.databinding.ActivityMainBinding
import io.github.skincanorg.skincan.lib.Extension.readJson
import io.github.skincanorg.skincan.ui.camera.CameraActivity
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(CreateMethod.INFLATE)
    private var file: File? = null
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter(applicationContext.assets.readJson("news.json").getJSONArray("news"))
    }

    @Inject
    lateinit var prefs: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (prefs.getToken() == null) {
            startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
            finish()
        } else {
            setupNews()
            setupBottomNavigation()
        }
    }

    private fun setupNews() {
        binding.rvNews.apply {
            setHasFixedSize(true)
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> true
                R.id.camera -> {
                    launcherIntentCameraX.launch(Intent(this@MainActivity, CameraActivity::class.java))
                    false // Do not highlight
                }
                else -> false
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // TODO: I don't know if this is needed just yet.
        if (it.resultCode == RESULT_SUCCESS) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            file = myFile
//
//            val result = Util.rotateCapturedImage(
//                BitmapFactory.decodeFile(myFile.path),
//                isBackCamera
//            )

            // binding.ivPreview.setImageBitmap(result)
        }
    }

    companion object {
        const val RESULT_SUCCESS = 200
    }
}