/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.skincanorg.skincan.data.auth.AuthLiveData

class UserRepository(private val auth: FirebaseAuth) {

    fun getFirebaseAuthState(): AuthLiveData = AuthLiveData(auth)

    fun getUser() = auth.currentUser as FirebaseUser

    fun login(email: String, password: String) = auth.signInWithEmailAndPassword(email, password)

    fun login(credential: AuthCredential) = auth.signInWithCredential(credential)

    fun logout() = auth.signOut()

    fun register(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)

    fun register(credential: AuthCredential) = login(credential)
}
