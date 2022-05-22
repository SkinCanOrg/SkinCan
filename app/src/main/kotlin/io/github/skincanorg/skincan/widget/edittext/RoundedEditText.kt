package io.github.skincanorg.skincan.widget.edittext

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.dp

// TODO: Validate-able EditText
class RoundedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int? = null,
) : AppCompatEditText(context, attrs, defStyleAttr ?: android.R.attr.editTextStyle) {

    init {
        this.setPadding(
            paddingLeft + 16.dp(context),
            paddingTop + 8.dp(context),
            paddingRight + 16.dp(context),
            paddingBottom + 8.dp(context)
        )
        compoundDrawablePadding = 16.dp(context)
        background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_clickable_focusable)
        this.setHintTextColor(ContextCompat.getColor(context, R.color.editTextColor))
        this.setTextColor(ContextCompat.getColor(context, R.color.edittext_color))
    }
}