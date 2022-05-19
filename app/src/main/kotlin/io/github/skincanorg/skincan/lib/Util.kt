package io.github.skincanorg.skincan.lib

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

object Util {
    fun getDrawableWithAttrTint(ctx: Context, resId: Int, @AttrRes colorId: Int): Drawable? {
        val typedValue = TypedValue()
        ctx.theme.resolveAttribute(colorId, typedValue, true)
        return getDrawableWithTint(ctx, resId, typedValue.data)
    }

    fun getDrawableWithTint(ctx: Context, resId: Int, @ColorRes colorId: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(ctx, resId)
        if (drawable != null) {
            val wrapped = DrawableCompat.wrap(drawable)
            val color = try {
                ContextCompat.getColor(ctx, colorId)
            } catch (exc: Resources.NotFoundException) {
                colorId
            }
            DrawableCompat.setTint(wrapped.mutate(), color)
            return wrapped
        }
        return null
    }

    fun isNightModeOn(context: Context): Boolean {
        return when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }
}