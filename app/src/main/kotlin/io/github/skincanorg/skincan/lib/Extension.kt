/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.lib

import android.content.Context
import android.content.res.AssetManager
import android.util.TypedValue
import org.json.JSONObject

object Extension {
    // DP to Pixel
    fun Int.dp(context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), context.resources.displayMetrics).toInt()

    fun Int.sp(context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
        this.toFloat(), context.resources.displayMetrics)

    fun AssetManager.readFile(fileName: String) = open(fileName)
        .bufferedReader()
        .use { it.readText() }

    fun AssetManager.readJson(fileName: String) = JSONObject(this.readFile(fileName))
}