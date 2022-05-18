package io.github.skincanorg.skincan.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.onboarding.OnboardAdapter
import io.github.skincanorg.skincan.data.onboarding.OnboardScreen
import io.github.skincanorg.skincan.databinding.ActivityOnboardingBinding

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private val binding: ActivityOnboardingBinding by viewBinding(CreateMethod.INFLATE)
    private var currentPosition = 0
    private val onboardAdapter by lazy {
        OnboardAdapter(
            applicationContext,
            listOf(
                OnboardScreen(R.drawable.illustration, R.string.onboard_title1, R.string.onboard_subtitle1),
                OnboardScreen(R.drawable.illustration2, R.string.onboard_title2, R.string.onboard_subtitle2)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Add page indicator
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            onboardingContainer.apply {
                adapter = onboardAdapter
                setCurrentItem(0, false)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        currentPosition = position
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
                })
            }
        }
    }
}