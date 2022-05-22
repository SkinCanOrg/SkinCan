/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import io.github.skincanorg.skincan.databinding.ActivityLoginBinding
import io.github.skincanorg.skincan.ui.MainActivity
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by viewBinding(CreateMethod.INFLATE)

    @Inject
    lateinit var prefs: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            btnLogin.setOnClickListener {
                // TODO: Implement an actual login function once API is created
                prefs.setToken("placeholder")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finishAffinity()
            }

            btnGotoRegisterContainer.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }
}