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
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.onboarding.OnboardAdapter
import io.github.skincanorg.skincan.data.onboarding.OnboardScreen
import io.github.skincanorg.skincan.databinding.ActivityOnboardingBinding
import io.github.skincanorg.skincan.ui.auth.LoginActivity

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private val binding: ActivityOnboardingBinding by viewBinding(CreateMethod.INFLATE)
    private var currentPosition = 0
    private val onboardAdapter by lazy {
        OnboardAdapter(
            applicationContext,
            listOf(
                OnboardScreen(R.drawable.illustration, R.string.onboard_title1, R.string.onboard_subtitle1),
                OnboardScreen(R.drawable.illustration2, R.string.onboard_title2, R.string.onboard_subtitle2),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOnboarding()
        setupCoreFunctions()
    }

    private fun setupOnboarding() {
        // TODO: Auto Scroll
        binding.apply {
            onboardingContainer.apply {
                adapter = onboardAdapter
                setCurrentItem(1, false)
                registerOnPageChangeCallback(
                    object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            currentPosition = position
                            when (currentPosition) {
                                0 -> {
                                    setIndicator(onboardAdapter.itemCount - 2)
                                }

                                onboardAdapter.itemCount - 1 -> {
                                    setIndicator(0)
                                }

                                else -> {
                                    setIndicator(currentPosition - 1)
                                }
                            }
                        }

                        override fun onPageScrollStateChanged(state: Int) {
                            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                                if (currentPosition == 0) {
                                    setCurrentItem(onboardAdapter.itemCount - 2, false)
                                } else if (currentPosition == onboardAdapter.itemCount - 1) {
                                    setCurrentItem(1, false)
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    private fun setupCoreFunctions() {
        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setIndicator(position: Int) {
        for ((index, indicator) in listOf(binding.indicatorPage1, binding.indicatorPage2).withIndex()) {
            indicator.setBackgroundResource(
                if (index == position) R.drawable.bg_indicator_selected
                else R.drawable.bg_indicator,
            )
        }
    }
}
