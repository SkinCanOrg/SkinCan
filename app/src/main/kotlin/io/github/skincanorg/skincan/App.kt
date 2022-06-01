/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.HiltAndroidApp
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver {
    @Inject
    lateinit var prefs: PreferencesHelper

    override fun onCreate() {
        super<Application>.onCreate()

        prefs.isDarkMode(applicationContext).asFlow().onEach {
            AppCompatDelegate.setDefaultNightMode(
                when (it) {
                    true -> AppCompatDelegate.MODE_NIGHT_YES
                    false -> AppCompatDelegate.MODE_NIGHT_NO
                },
            )
        }.launchIn(ProcessLifecycleOwner.get().lifecycleScope)
    }
}
