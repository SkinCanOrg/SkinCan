package io.github.skincanorg.skincan.ui.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.PreferenceExtension.newScreen
import io.github.skincanorg.skincan.lib.PreferenceExtension.switchPreference
import io.github.skincanorg.skincan.lib.PreferenceExtension.titleRes
import io.github.skincanorg.skincan.lib.Util
import io.github.skincanorg.skincan.data.preference.PreferenceKeys as Keys

class ProfileAndCategoryFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        preferenceScreen = preferenceManager.newScreen {
            switchPreference {
                key = Keys.NIGHT_MODE
                titleRes = R.string.dark_mode
                isChecked = Util.isNightModeOn(context)
            }
        }
    }
}
