/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import io.github.skincanorg.skincan.R

class OneFourthCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleRes) {
    private val paint = Paint()
    private val rectF = RectF()

    init {
        if (attrs != null) {
            val attr = context.theme.obtainStyledAttributes(attrs, R.styleable.OneFourthCircleView, 0, 0)
            paint.color = attr.getColor(R.styleable.OneFourthCircleView_circleColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            rectF.set(width.toFloat() * -1, height.toFloat() * -1, width.toFloat(), height.toFloat())
            canvas.drawArc(rectF, 0f, 90f, true, paint)
        }
    }
}