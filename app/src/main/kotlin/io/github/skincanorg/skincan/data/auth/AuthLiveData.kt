/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.auth

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

class AuthLiveData(
    private val auth: FirebaseAuth,
) : LiveData<Boolean>(), FirebaseAuth.AuthStateListener {
    override fun onAuthStateChanged(auth: FirebaseAuth) {
        value = auth.currentUser != null
    }

    override fun onActive() {
        super.onActive()
        auth.addAuthStateListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        auth.removeAuthStateListener(this)
    }
}
