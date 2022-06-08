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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.data.model.AppResult
import io.github.skincanorg.skincan.databinding.ActivityLoginBinding
import io.github.skincanorg.skincan.ui.main.MainActivity
import io.github.skincanorg.skincan.widget.dialog.LoginAlertDialog
import io.github.skincanorg.skincan.widget.edittext.ValidateEditText
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupCoreFunctions()
    }

    private fun setupCoreFunctions() {
        binding.apply {
            val validateList = listOf(etEmail, etPassword)
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

            viewModel.loginState.observe(this@LoginActivity) { state ->
                when (state) {
                    is AppResult.Error -> {
                        if (state.message.startsWith("ERROR_"))
                            LoginAlertDialog(this@LoginActivity).apply {
                                illustrationRes = R.drawable.dialog_illustration_fail
                                titleRes = R.string.login_fail
                                descriptionRes = when (state.message) {
                                    "ERROR_USER_NOT_FOUND" -> R.string.login_fail_desc
                                    "ERROR_WRONG_PASSWORD" -> R.string.login_fail_desc
                                    else -> R.string.auth_fail_desc
                                }
                                buttonTextRes = R.string.try_again
                            }.show()
                        else
                            Toast.makeText(this@LoginActivity, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                    }

                    is AppResult.Success -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finishAffinity()
                    }

                    else -> {}
                }
                validateList.forEach { it.isEnabled = state !is AppResult.Loading }
                btnLogin.apply {
                    isEnabled = state !is AppResult.Loading
                    isLoading = state is AppResult.Loading && state.data == 2
                }
                btnGoogleAuth.apply {
                    isEnabled = state !is AppResult.Loading
                    isLoading = state is AppResult.Loading && state.data == 1
                }
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

            btnGoogleAuth.apply {
                isStrokeMode = true
                setOnClickListener { launcherIntentGoogle.launch(googleSignInClient.signInIntent) }
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

    private val launcherIntentGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnSuccessListener {
                viewModel.login(it.idToken!!)
            }
        }
    }
}
