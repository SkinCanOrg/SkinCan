/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Editable
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.dp
import io.github.skincanorg.skincan.lib.Extension.setCursorDrawableColor


class ValidateEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int? = null,
) : TextInputLayout(context, attrs, defStyleAttr ?: com.google.android.material.R.attr.textInputStyle) {
    private val _editText: AppCompatEditText
    val editText get() = _editText
    var isInputRequired: Boolean

    var text: Editable?
        get() = _editText.text
        set(newValue) {
            _editText.text = newValue
        }

    fun interface OnValidateListener {
        fun onValidate(v: ValidateEditText): ValidationResult
    }

    data class ValidationResult(
        val errorString: String,
        val isValid: Boolean,
    ) {
        constructor(context: Context, @StringRes errorRes: Int, isValid: Boolean) : this(
            context.getString(errorRes),
            isValid,
        )

        constructor(context: Context, @StringRes errorRes: Int, format: List<Any>, isValid: Boolean) : this(
            context.getString(errorRes, *format.toTypedArray()),
            isValid,
        )
    }

    private var onValidateListener: OnValidateListener? = null

    fun setOnValidateListener(onValidateListener: OnValidateListener?) {
        this.onValidateListener = onValidateListener
    }

    init {
        errorIconDrawable = null // overlapping other icon

        boxStrokeWidth = 0
        boxStrokeWidthFocused = 0
        val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.ValidateEditText, 0, 0)

        isInputRequired = attr.getBoolean(R.styleable.ValidateEditText_inputRequired, false)

        this.setWillNotDraw(false)
        _editText = TextInputEditText(context)
        _editText.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPadding(
                paddingLeft + 16.dp(context),
                paddingTop + 6.dp(context),
                paddingRight + 16.dp(context),
                paddingBottom + 6.dp(context),
            )
            compoundDrawablePadding = 16.dp(context)
            background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_clickable_focusable)
            setCursorDrawableColor(ContextCompat.getColor(context, R.color.editTextFocusedColor))

            val initColor = R.color.editTextColor
            setHintTextColor(ContextCompat.getColor(context, initColor))
            setTextColor(ContextCompat.getColor(context, initColor))
            this@ValidateEditText.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, initColor)))

            setOnFocusChangeListener { _, hasFocus ->
                val color = if (hasFocus) R.color.editTextFocusedColor else R.color.editTextColor
                setHintTextColor(ContextCompat.getColor(context, color))
                setTextColor(ContextCompat.getColor(context, color))
                this@ValidateEditText.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)))
            }

            addTextChangedListener { if (error != null || isErrorEnabled) clearError() }

            attr.getInt(R.styleable.ValidateEditText_android_inputType, 0).also {
                if (it != 0)
                    inputType = it
            }

            typeface = Typeface.SANS_SERIF
        }
        this.addView(_editText)
        this.isHintEnabled = false
    }

    private fun clearError() {
        error = null
        isErrorEnabled = false
    }

    fun validateInput(): ValidationResult {
        if (isInputRequired && text.isNullOrEmpty())
            return ValidationResult(
                context,
                R.string.input_cant_be_empty,
                listOf(editText.hint ?: "Input"),
                false,
            )
        return onValidateListener?.onValidate(this) ?: ValidationResult("No validation rules", true)
    }
}
