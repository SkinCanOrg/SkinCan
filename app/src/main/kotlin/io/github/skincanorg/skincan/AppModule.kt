/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import io.github.skincanorg.skincan.data.repository.ResultRepository
import io.github.skincanorg.skincan.data.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // --- [ Start of qualifier defines ]

    // --- [ End of qualifier defines ]

    @Singleton
    @Provides
    fun providesPreferences(@ApplicationContext context: Context) = PreferencesHelper(context)

    @Singleton
    @Provides
    fun providesRepository(): UserRepository = UserRepository()

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): Database =
        Database(AndroidSqliteDriver(Database.Schema, context, "database.db"))

    @Singleton
    @Provides
    fun providesResultRepository(database: Database): ResultRepository = ResultRepository(database)
}
