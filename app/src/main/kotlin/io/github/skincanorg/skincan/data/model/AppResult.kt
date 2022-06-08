/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.model

sealed class AppResult<out R> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Error<T>(val message: String, val data: T? = null) : AppResult<T>()
    data class Loading<T>(val data: T? = null) : AppResult<T>()
}
