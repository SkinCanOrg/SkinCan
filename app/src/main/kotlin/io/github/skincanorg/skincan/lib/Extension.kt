/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.lib

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object Extension {
    // DP to Pixel
    fun Int.dp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), context.resources.displayMetrics,
    ).toInt()

    fun Int.sp(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this.toFloat(), context.resources.displayMetrics,
    )

    fun AssetManager.readFile(fileName: String) = open(fileName)
        .bufferedReader()
        .use { it.readText() }

    fun AssetManager.readJson(fileName: String) = JSONObject(this.readFile(fileName))

    fun Uri.toFile(context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = Util.createTempFile(context)

        val inputStream = contentResolver.openInputStream(this) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun Bitmap.rotate(rotation: Float, flip: Boolean = false): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(rotation)
        if (flip)
            matrix.postScale(-1f, 1f)
        val rotatedBitmap = Bitmap.createBitmap(
            this, 0, 0,
            this.width,
            this.height,
            matrix,
            true,
        )
        this.recycle()
        return rotatedBitmap
    }

    fun String.toDate(
        dateFormat: String = "yyyy-MM-ddTHH:mm:ssZ",
        timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
    ): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this) as Date
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

    // [01 Jun 2022]: https://stackoverflow.com/a/59269370, yet another pain from Google.
    fun TextView.setCursorDrawableColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textCursorDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(color, color))
                .apply { setSize(2.sp(context).toInt(), textSize.toInt()) }
            return
        }

        try {
            val editorField = TextView::class.java.getFieldByName("mEditor")
            val editor = editorField?.get(this) ?: this
            val editorClass: Class<*> = if (editorField != null) editor.javaClass else TextView::class.java
            val cursorRes = TextView::class.java.getFieldByName("mCursorDrawableRes")?.get(this) as? Int ?: return

            val tintedCursorDrawable = Util.getDrawableWithTint(context, cursorRes, color) ?: return

            val cursorField = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                editorClass.getFieldByName("mDrawableForCursor")
            } else {
                null
            }
            if (cursorField != null) {
                cursorField.set(editor, tintedCursorDrawable)
            } else {
                editorClass.getFieldByName("mCursorDrawable", "mDrawableForCursor")
                    ?.set(editor, arrayOf(tintedCursorDrawable, tintedCursorDrawable))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    // [01 Jun 2022]: https://stackoverflow.com/a/59269370.
    private fun Class<*>.getFieldByName(vararg name: String): Field? {
        name.forEach {
            try {
                return this.getDeclaredField(it).apply { isAccessible = true }
            } catch (_: Throwable) {
            }
        }
        return null
    }

    fun Long.toDateTime(pattern: String): String =
        DateTimeFormatter.ofPattern(pattern)
            .format(Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()))
}
