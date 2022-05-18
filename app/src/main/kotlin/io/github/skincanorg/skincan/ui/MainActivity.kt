package io.github.skincanorg.skincan.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import io.github.skincanorg.skincan.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(CreateMethod.INFLATE)

    @Inject
    lateinit var prefs: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // TODO: Check if logged in
        if (prefs.getToken() == null) {
            startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
        } else {
            // Logged in
        }
    }
}