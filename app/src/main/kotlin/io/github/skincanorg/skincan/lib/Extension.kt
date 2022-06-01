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
import android.net.Uri
import android.util.TypedValue
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
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

}
