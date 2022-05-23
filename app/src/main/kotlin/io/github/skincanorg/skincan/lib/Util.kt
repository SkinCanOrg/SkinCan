/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.lib

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.readFile
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

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

    fun makeToastShort(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun makeToastLong(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun createFile (application: Application): File{
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())


    fun rotateCapturedImage(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()

        return if (isBackCamera) {
            // matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            // matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }
}