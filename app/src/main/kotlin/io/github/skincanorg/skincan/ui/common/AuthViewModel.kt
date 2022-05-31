/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skincanorg.skincan.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: UserRepository) : ViewModel() {
    val authState: LiveData<Boolean> = repo.getFirebaseAuthState()

    fun login(email: String, password: String) = repo.login(email, password)

    fun logout() = repo.logout()

    fun getUser() = repo.getUser()
}
