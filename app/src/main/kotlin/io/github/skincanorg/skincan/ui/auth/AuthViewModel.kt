/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skincanorg.skincan.data.model.AppResult
import io.github.skincanorg.skincan.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: UserRepository) : ViewModel() {
    val authState: LiveData<Boolean> = repo.getFirebaseAuthState()

    fun getUser() = repo.getUser()

    /* LoginMethod:
     * - null = Failed
     * - 0 = Custom Token
     * - 1 = GoogleAuth
     * - 2 = Password Login
     */
    private var _loginState = MutableLiveData<AppResult<Int>>()
    val loginState: LiveData<AppResult<Int>> = _loginState

    fun login(email: String, password: String) {
        _loginState.postValue(AppResult.Loading(2))
        repo.login(email, password).addOnSuccessListener {
            _loginState.postValue(AppResult.Success(2))
        }.addOnFailureListener { exc ->
            val reason = if (exc is FirebaseAuthException)
                exc.errorCode
            else
                exc.localizedMessage
            _loginState.postValue(AppResult.Error(reason, null))
        }
    }

    fun login(token: String, method: Int = 1) {
        _loginState.postValue(AppResult.Loading(method))
        when (method) {
            1 -> {
                repo.login(GoogleAuthProvider.getCredential(token, null))
                    .addOnSuccessListener {
                        _loginState.postValue(AppResult.Success(method))
                    }.addOnFailureListener {
                        _loginState.postValue(AppResult.Error("hmm", null))
                    }
            }

            else -> {}
        }
    }

    fun logout() = repo.logout()

    private var _registerState = MutableLiveData<AppResult<Int>>()
    val registerState: LiveData<AppResult<Int>> = _registerState

    fun register(name: String, email: String, password: String) {
        _registerState.postValue(AppResult.Loading())
        repo.register(email, password).addOnSuccessListener {
            getUser().updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build(),
            ).addOnSuccessListener {
                logout() // Our user-flow is to redirect them to login page after register
                _registerState.postValue(AppResult.Success(2))
            }
        }.addOnFailureListener { exc ->
            val reason = if (exc is FirebaseAuthException)
                exc.errorCode
            else
                exc.localizedMessage
            _registerState.postValue(AppResult.Error(reason, null))
        }
    }

    fun register(token: String, method: Int = 1) {
        _registerState.postValue(AppResult.Loading(method))
        when (method) {
            1 -> {
                repo.login(GoogleAuthProvider.getCredential(token, null))
                    .addOnSuccessListener {
                        _registerState.postValue(AppResult.Success(method))
                    }.addOnFailureListener {
                        _registerState.postValue(AppResult.Error("hmm", null))
                    }
            }

            else -> {}
        }
    }

    private var _reloadState = MutableLiveData<AppResult<Boolean>>()
    val reloadState: LiveData<AppResult<Boolean>> = _reloadState

    fun reload() {
        _reloadState.postValue(AppResult.Loading())
        getUser().reload().addOnFailureListener {
            logout()
            _reloadState.postValue(AppResult.Error("", null))
        }
    }
}
