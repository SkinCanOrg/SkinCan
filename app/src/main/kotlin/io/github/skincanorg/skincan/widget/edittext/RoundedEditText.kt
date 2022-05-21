package io.github.skincanorg.skincan.widget.edittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.px

class RoundedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int? = null,
) : AppCompatEditText(context, attrs, defStyleAttr ?: android.R.attr.editTextStyle) {

    init {
        this.setPadding(
            paddingLeft + 16.px(context),
            paddingTop + 8.px(context),
            paddingRight + 16.px(context),
            paddingBottom + 8.px(context)
        )
        compoundDrawablePadding = 16.px(context)
        background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_clickable_focusable)
        this.setHintTextColor(ContextCompat.getColor(context, R.color.editTextColor))
        this.setTextColor(ContextCompat.getColor(context, R.color.edittext_color))
    }
}