/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardScreen(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val subtitle: Int
)