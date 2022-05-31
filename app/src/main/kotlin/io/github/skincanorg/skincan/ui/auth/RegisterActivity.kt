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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.databinding.ActivityRegisterBinding
import io.github.skincanorg.skincan.ui.common.AuthViewModel
import io.github.skincanorg.skincan.widget.dialog.LoginAlertDialog

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            btnRegister.setOnClickListener {
                btnRegister.isEnabled = false
                btnGotoLoginContainer.isEnabled = false

                // TODO: Validate input
                // TODO: Username or Display name input
                viewModel.register(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                ).addOnCompleteListener { task ->
                    btnRegister.isEnabled = true
                    btnGotoLoginContainer.isEnabled = true
                    if (task.isSuccessful) {
                        val alert = LoginAlertDialog(this@RegisterActivity)
                        alert.setOnLoginListener {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }.show()
                    } else {
                        // TODO: Tell user that registration failed
                    }
                }
            }

            btnGotoLoginContainer.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
