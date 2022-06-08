/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget.button

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.dp
import io.github.skincanorg.skincan.lib.Extension.sp
import io.github.skincanorg.skincan.lib.Util.getDrawableWithAttrTint

open class RoundedButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int? = null,
) : AppCompatButton(context, attrs, defStyleAttr ?: android.R.attr.buttonStyle) {
    init {
        background = getDrawableWithAttrTint(context, R.drawable.bg_rounded_ripple, R.attr.colorPrimary)
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorOnPrimary, typedValue, true)
        this.setTextColor(typedValue.data)
        this.setPadding(
            paddingLeft + 16.dp(context),
            paddingTop + 6.dp(context),
            paddingRight + 16.dp(context),
            paddingBottom + 6.dp(context),
        )
        textSize = 18.sp(context)
    }
}
