/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget.button

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.databinding.LoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: LoadingButtonBinding
    var text: String? = null

    @ColorInt
    var buttonColor: Int = 0

    @ColorInt
    var textColor: Int = 0

    var isStrokeMode: Boolean
        get() = false
        set(value) {
            if (!value)
                return
            binding.apply {
                button.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_ripple_stroke)
                    val typedValue = TypedValue()
                    context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                    setTextColor(typedValue.data)
                    loading.indeterminateTintList = ColorStateList.valueOf(typedValue.data)
                }
            }
        }

    init {
        binding = LoadingButtonBinding.inflate(LayoutInflater.from(context), this, true)

        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
        text = attr.getString(R.styleable.LoadingButton_android_text)
        buttonColor = attr.getColor(R.styleable.LoadingButton_buttonColor, 0)
        textColor = attr.getColor(R.styleable.LoadingButton_android_textColor, 0)
        drawButton()
    }

    private fun drawButton() {
        binding.apply {
            button.text = text

            if (textColor != 0) {
                button.setTextColor(textColor)
                loading.indeterminateTintList = ColorStateList.valueOf(textColor)
            }

            if (buttonColor != 0)
                button.backgroundTintList = ColorStateList.valueOf(buttonColor)
        }
    }

    var isLoading: Boolean
        get() = binding.loading.isVisible
        set(newValue) {
            if (newValue)
                onStartLoading()
            else
                onStopLoading()
        }

    private fun onStartLoading() {
        binding.apply {
            button.apply {
                text = ""
                isClickable = false
            }
            loading.isVisible = true
        }
    }

    private fun onStopLoading() {
        binding.apply {
            button.apply {
                text = this@LoadingButton.text
                isClickable = true
            }
            loading.isVisible = false
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        binding.button.setOnClickListener {
            if (!isLoading)
                onClickListener?.onClick(it)
        }
    }
}
