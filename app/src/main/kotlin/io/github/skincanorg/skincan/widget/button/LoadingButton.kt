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
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import io.github.skincanorg.skincan.R

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    private var button: RoundedButton
    private var progressBar: ProgressBar
    var text: String? = null

    @ColorInt
    var buttonColor: Int = 0

    @ColorInt
    var textColor: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.loading_button, this, true)
        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.loading)

        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
        text = attr.getString(R.styleable.LoadingButton_buttonText)
        buttonColor = attr.getColor(R.styleable.LoadingButton_buttonColor, 0)
        textColor = attr.getColor(R.styleable.LoadingButton_textColor, 0)
        drawButton()
    }

    private fun drawButton() {
        button.text = text

        if (textColor != 0) {
            button.setTextColor(textColor)
            progressBar.indeterminateTintList = ColorStateList.valueOf(textColor)
        }
    }

    var isLoading: Boolean
        get() = progressBar.visibility == View.VISIBLE
        set(newValue) {
            if (newValue)
                onStartLoading()
            else
                onStopLoading()
        }

    private fun onStartLoading() {
        button.apply {
            text = ""
            isClickable = false
        }
        progressBar.visibility = View.VISIBLE
    }

    private fun onStopLoading() {
        button.apply {
            text = this@LoadingButton.text
            isClickable = true
        }
        progressBar.visibility = View.GONE
    }

    fun setOnButtonClickListener(onClickListener: OnClickListener?) {
        button.setOnClickListener {
            if (!isLoading)
                onClickListener?.onClick(it)
        }
    }
}
