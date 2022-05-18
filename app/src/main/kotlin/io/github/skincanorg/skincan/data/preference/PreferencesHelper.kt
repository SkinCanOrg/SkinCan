package io.github.skincanorg.skincan.data.preference

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.skincanorg.skincan.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_${context.getString(R.string.app_name)}", Context.MODE_PRIVATE)

    fun getToken() = prefs.getString("token", null)
}