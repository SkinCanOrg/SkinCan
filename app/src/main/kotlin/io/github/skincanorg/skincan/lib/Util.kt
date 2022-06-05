/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.lib

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.exifinterface.media.ExifInterface
import io.github.skincanorg.skincan.R
import io.github.skincanorg.skincan.lib.Extension.rotate
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

object Util {
    const val API_URL = "http://127.0.0.1/"

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
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun makeToastLong(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG,
        ).show()
    }

    fun createTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun createFile(application: Application): File {
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
        Locale.US,
    ).format(System.currentTimeMillis())

    fun processBitmap(path: String): Bitmap = processBitmap(BitmapFactory.decodeFile(path), File(path))

    fun processBitmap(bitmap: Bitmap, file: File): Bitmap {
        val exif = FileInputStream(file).use { ExifInterface(it) }

        return when (val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> bitmap.rotate(0f, true)
            ExifInterface.ORIENTATION_ROTATE_180 -> bitmap.rotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> bitmap.rotate(90f, true)
            ExifInterface.ORIENTATION_TRANSPOSE -> bitmap.rotate(90f, true)
            ExifInterface.ORIENTATION_ROTATE_90 -> bitmap.rotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> bitmap.rotate(-90f, true)
            ExifInterface.ORIENTATION_ROTATE_270 -> bitmap.rotate(270f)
            else -> {
                Log.d("orientation", orientation.toString())
                bitmap
            }
        }
    }

    fun getCacheDir(context: Context, dir: String): File {
        return context.getExternalFilesDir(dir)
            ?: File(context.filesDir, dir).also { it.mkdirs() }
    }
}
