/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.result

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.ActivityResultBinding
import io.github.skincanorg.skincan.lib.Extension.toDateTime
import io.github.skincanorg.skincan.lib.Util
import io.github.skincanorg.skincan.ui.camera.CameraActivity
import io.github.skincanorg.skincan.ui.main.MainActivity
import com.bumptech.glide.request.target.Target as GlideTarget

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private val binding: ActivityResultBinding by viewBinding(CreateMethod.INFLATE)
    private val from by lazy { intent.extras?.getInt(FROM) ?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportPostponeEnterTransition()
        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            appbar.isVisible = from == 1
            if (from == 1) {
                setSupportActionBar(appbar)
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
                appbar.setNavigationOnClickListener { onBackPressed() }
            }
            btnGroup.isVisible = from == 0

            btnHome.setOnClickListener { onBackPressed() }
            btnScanAgain.setOnClickListener {
                startActivity(
                    Intent(this@ResultActivity, CameraActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    },
                )
            }

            val imagePath = intent.extras?.getString(PHOTO_PATH)
            if (imagePath != null) {
                val img = Util.processBitmap(imagePath)
                val transitionName = intent.extras?.getString(ID)

                if (transitionName != null) {
                    pictWrapper.transitionName = transitionName

                    ivResultPict.load(img) {
                        supportStartPostponedEnterTransition()
                    }

                    val sharedElementTransitionSet = TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeTransform())
                        .addTransition(ChangeClipBounds())
                        .addTransition(ChangeImageTransform())

                    val transitionSet = Fade().apply {
                        excludeTarget(android.R.id.statusBarBackground, true)
                        excludeTarget(android.R.id.navigationBarBackground, true)
                        excludeTarget(R.id.appbar, true)
                    }

                    window.sharedElementReturnTransition = sharedElementTransitionSet
                    window.sharedElementEnterTransition = sharedElementTransitionSet
                    window.enterTransition = transitionSet
                    window.returnTransition = transitionSet
                } else {
                    ivResultPict.load(img)
                }
            }

            when (val result = intent.extras?.getString(RESULT)) {
                "Clear" -> {
                    tvResultStatus.isEnabled = true
                    tvResultStatus.text = "Clear"
                    tvResultDesc.text = "Great!\nYou’re clear from skin cancer\n\nStay safe from cancer risk potential"
                }

                null -> {
                    tvResultStatus.isEnabled = false
                    tvResultStatus.text = "ERROR"
                    tvResultDesc.text = "Uh oh! Something went wrong!"
                }

                else -> {
                    tvResultStatus.isEnabled = false
                    tvResultStatus.text = "Cancer"
                    tvResultDesc.text =
                        "We found potential risk cancer on your body. ($result)\n\n" +
                            "You can check some tips here or\nconsult to the doctor"
                }
            }

            tvResultTime.text = getString(
                R.string.result_scanned_at,
                (intent.extras?.getLong(TIMESTAMP) ?: 0L).toDateTime("d MMM YYYY, HH:mm"),
            )

        }
    }

    private fun ImageView.load(img: Bitmap, onLoadingFinished: () -> Unit = {}) {
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: GlideTarget<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                onLoadingFinished()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: GlideTarget<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                onLoadingFinished()
                return false
            }
        }

        val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_picture)
            .dontTransform()

        Glide.with(this)
            .load(img)
            .apply(requestOptions)
            .listener(listener)
            .into(this)
    }

    override fun onBackPressed() {
        if (from == 0)
            startActivity(
                Intent(this@ResultActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                },
            )
        else
            finishAfterTransition()
    }

    companion object {
        const val ID = "ID"
        const val PHOTO_PATH = "PHOTO_PATH"
        const val RESULT = "IS_CANCER"
        const val TIMESTAMP = "TIMESTAMP"
        const val FROM = "FROM" // 0 = Scanner, 1 = List
    }
}
