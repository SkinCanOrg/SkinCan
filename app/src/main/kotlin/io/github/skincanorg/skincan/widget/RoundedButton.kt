/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.px
import io.github.skincanorg.skincan.lib.Util.getDrawableWithAttrTint

class RoundedButton @JvmOverloads constructor(
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
            paddingLeft + 16.px(context),
            paddingTop + 8.px(context),
            paddingRight + 16.px(context),
            paddingBottom + 8.px(context)
        )
        textSize = 18.px(context).toFloat()
    }
}