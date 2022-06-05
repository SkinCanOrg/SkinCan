/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget.edittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.dp
import io.github.skincanorg.skincan.lib.Extension.setCursorDrawableColor

class RoundedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int? = null,
) : AppCompatEditText(context, attrs, defStyleAttr ?: android.R.attr.editTextStyle) {

    init {
        this.setPadding(
            paddingLeft + 16.dp(context),
            paddingTop + 6.dp(context),
            paddingRight + 16.dp(context),
            paddingBottom + 6.dp(context),
        )
        compoundDrawablePadding = 16.dp(context)
        background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_clickable_focusable)
        this.setCursorDrawableColor(ContextCompat.getColor(context, R.color.editTextFocusedColor))
        this.setHintTextColor(ContextCompat.getColor(context, R.color.editTextColor))
        this.setTextColor(ContextCompat.getColor(context, R.color.editTextColor))
        setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) R.color.editTextFocusedColor else R.color.editTextColor

            this.setHintTextColor(ContextCompat.getColor(context, color))
            this.setTextColor(ContextCompat.getColor(context, color))
        }
    }
}
