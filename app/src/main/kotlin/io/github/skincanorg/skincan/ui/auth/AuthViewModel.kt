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
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skincanorg.skincan.data.model.AppResult
import io.github.skincanorg.skincan.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: UserRepository) : ViewModel() {
    val authState: LiveData<Boolean> = repo.getFirebaseAuthState()

    fun getUser() = repo.getUser()

    private var _loginState = MutableLiveData<AppResult<Boolean>>()
    val loginState: LiveData<AppResult<Boolean>> = _loginState

    fun login(email: String, password: String) {
        _loginState.postValue(AppResult.Loading)
        repo.login(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _loginState.postValue(AppResult.Success(true))
            } else {
                _loginState.postValue(AppResult.Error("RIP", null))
            }
        }
    }

    fun logout() = repo.logout()

    private var _registerState = MutableLiveData<AppResult<Boolean>>()
    val registerState: LiveData<AppResult<Boolean>> = _registerState

    fun register(name: String, email: String, password: String) {
        _registerState.postValue(AppResult.Loading)
        repo.register(email, password).addOnSuccessListener { result ->
            getUser().updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build(),
            ).addOnSuccessListener {
                logout() // Our user-flow is to redirect them to login page after register
                _registerState.postValue(AppResult.Success(true))
            }
        }.addOnFailureListener { exc ->
            val reason = if (exc is FirebaseAuthException)
                "ERR-" + exc.errorCode
            else
                exc.localizedMessage
            _registerState.postValue(AppResult.Error(reason, false))
        }
    }
}
