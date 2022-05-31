package io.github.skincanorg.skincan.ui.preference

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.skincanorg.skincan.lib.PreferenceExtension.newScreen
import io.github.skincanorg.skincan.lib.PreferenceExtension.preference

class ProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        preferenceScreen = preferenceManager.newScreen {
            // TODO: Add icon
            preference {
                key = "account"
                title = "Account Settings"
            }
            preference {
                key = "preferences"
                title = "Preferences"
                intent = Intent(requireActivity(), PreferenceActivity::class.java)
            }
            preference {
                key = "about"
                title = "About"
            }
        }
    }
}
