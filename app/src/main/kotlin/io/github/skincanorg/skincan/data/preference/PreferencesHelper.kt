/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import io.github.skincanorg.skincan.data.preference.PreferenceKeys as Keys

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val flowPrefs: FlowSharedPreferences = FlowSharedPreferences(prefs)

    fun isDarkMode(context: Context) = flowPrefs.getInt(Keys.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun getToken() = prefs.getString(Keys.TOKEN, null)

    fun setToken(token: String) = prefs.edit().putString(Keys.TOKEN, token).commit()

    fun clearToken() = prefs.edit().remove(Keys.TOKEN).commit()
}
