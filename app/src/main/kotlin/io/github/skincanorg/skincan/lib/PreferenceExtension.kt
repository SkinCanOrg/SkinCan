package io.github.skincanorg.skincan.lib

import androidx.preference.*

object PreferenceExtension {
    inline fun PreferenceManager.newScreen(block: PreferenceScreen.() -> Unit): PreferenceScreen {
        return createPreferenceScreen(context).also { it.block() }
    }

    inline fun PreferenceGroup.preference(block: Preference.() -> Unit): Preference {
        return initThenAdd(Preference(context), block)
    }

    inline fun PreferenceGroup.switchPreference(block: SwitchPreference.() -> Unit): SwitchPreference {
        return initThenAdd(SwitchPreference(context), block)
    }

    inline fun <P: Preference> PreferenceGroup.initThenAdd(preference: P, block: P.() -> Unit): P {
        return preference.apply {
            block()
            addPreference(this)
        }
    }

    var Preference.titleRes: Int
        get() = 0
        set(newValue) { this.title = context.getString(newValue) }
}
