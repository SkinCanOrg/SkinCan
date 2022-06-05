package io.github.skincanorg.skincan.lib

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.*

object PreferenceExtension {
    inline fun PreferenceManager.newScreen(block: PreferenceScreen.() -> Unit): PreferenceScreen {
        return createPreferenceScreen(context).also { it.block() }
    }

    inline fun PreferenceGroup.preference(block: Preference.() -> Unit): Preference {
        return initThenAdd(Preference(context), block)
    }

    inline fun PreferenceGroup.switchPreference(block: SwitchPreferenceCompat.() -> Unit): SwitchPreferenceCompat {
        return initThenAdd(SwitchPreferenceCompat(context), block)
    }

    inline fun PreferenceGroup.preferenceCategory(block: PreferenceCategory.() -> Unit): PreferenceCategory {
        return initThenAdd(PreferenceCategory(context), block)
    }

    inline fun <P : Preference> PreferenceGroup.initThenAdd(preference: P, block: P.() -> Unit): P {
        return preference.apply {
            block()
            addPreference(this)
        }
    }

    var Preference.titleRes: Int
        get() = 0
        set(@StringRes newValue) {
            this.title = context.getString(newValue)
        }

    var Preference.iconRes: Int
        get() = 0
        set(@DrawableRes newValue) {
            this.icon = ContextCompat.getDrawable(context, newValue)
        }
}
