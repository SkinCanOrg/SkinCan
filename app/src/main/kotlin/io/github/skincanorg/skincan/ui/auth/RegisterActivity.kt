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
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.model.AppResult
import io.github.skincanorg.skincan.databinding.ActivityRegisterBinding
import io.github.skincanorg.skincan.widget.dialog.LoginAlertDialog
import io.github.skincanorg.skincan.widget.edittext.ValidateEditText

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
            val validateList = listOf(etName, etEmail, etPassword)
            etEmail.setOnValidateListener {
                ValidateEditText.ValidationResult(
                    applicationContext,
                    R.string.invalid_email,
                    android.util.Patterns.EMAIL_ADDRESS.matcher(it.text.toString()).matches(),
                )
            }
            etPassword.setOnValidateListener {
                ValidateEditText.ValidationResult(
                    applicationContext,
                    R.string.password_length,
                    it.text!!.length >= 8,
                )
            }

            viewModel.registerState.observe(this@RegisterActivity) { state ->
                when (state) {
                    is AppResult.Error -> {
                        Log.d("ziRegisterFail", state.message)
                        LoginAlertDialog(this@RegisterActivity).apply {
                            illustrationRes = R.drawable.dialog_illustration_fail
                            titleRes = R.string.register_fail
                            descriptionRes = R.string.register_fail_desc  // TODO: Specify the error?
                            buttonTextRes = R.string.try_again
                        }.show()
                    }
                    is AppResult.Success -> {
                        LoginAlertDialog(this@RegisterActivity).apply {
                            setOnLoginListener {
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                        }.show()
                    }
                    else -> {}
                }
                btnRegister.isLoading = state is AppResult.Loading
                btnGotoLoginContainer.isEnabled = state !is AppResult.Loading
            }

            btnRegister.setOnClickListener {
                var isValid = true

                validateList.forEach {
                    val res = it.validateInput()
                    isValid = isValid and res.isValid
                    if (!res.isValid)
                        it.error = res.errorString
                }

                if (isValid)
                    viewModel.register(etName.text.toString(), etEmail.text.toString(), etPassword.text.toString())
            }

            btnGotoLoginContainer.setOnClickListener {
                startActivity(
                    Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    },
                )
            }
        }
    }
}
