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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.model.AppResult
import io.github.skincanorg.skincan.databinding.ActivityLoginBinding
import io.github.skincanorg.skincan.ui.main.MainActivity
import io.github.skincanorg.skincan.widget.edittext.ValidateEditText

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            val validateList = listOf(etEmail, etPassword)
            etEmail.setOnValidateListener {
                if (it.text.isNullOrEmpty())
                    ValidateEditText.ValidationResult(
                        applicationContext,
                        R.string.input_cant_be_empty,
                        listOf(it.hint ?: "Email"),
                        false,
                    )
                else
                    ValidateEditText.ValidationResult(
                        applicationContext,
                        R.string.invalid_email,
                        android.util.Patterns.EMAIL_ADDRESS.matcher(it.text.toString()).matches(),
                    )
            }
            etPassword.setOnValidateListener {
                if (it.text.isNullOrEmpty())
                    ValidateEditText.ValidationResult(
                        applicationContext,
                        R.string.input_cant_be_empty,
                        listOf(it.hint ?: "Password"),
                        false,
                    )
                else
                    ValidateEditText.ValidationResult(
                        applicationContext,
                        R.string.password_length,
                        it.text!!.length >= 8,
                    )
            }

            viewModel.loginState.observe(this@LoginActivity) { state ->
                when (state) {
                    is AppResult.Error -> {
                        // TODO: Inform the user why login is unsuccessful
                        Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                    }
                    is AppResult.Success -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                    else -> {}
                }
                btnLogin.isLoading = state is AppResult.Loading
                btnGotoRegisterContainer.isEnabled = state !is AppResult.Loading
            }

            btnLogin.setOnClickListener {
                var isValid = true

                validateList.forEach {
                    val res = it.validateInput()
                    isValid = isValid and res.isValid
                    if (!res.isValid)
                        it.error = res.errorString
                }

                if (isValid)
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
            }

            btnGotoRegisterContainer.setOnClickListener {
                startActivity(
                    Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    },
                )
            }
        }
    }
}
