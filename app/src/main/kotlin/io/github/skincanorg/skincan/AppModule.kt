/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
    @Singleton
    @Provides
    fun providesPreferences(@ApplicationContext context: Context) = PreferencesHelper(context)

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun providesRepository(auth: FirebaseAuth): UserRepository = UserRepository(auth)

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): Database =
        Database(AndroidSqliteDriver(Database.Schema, context, "database.db"))

    @Singleton
    @Provides
    fun providesResultRepository(database: Database): ResultRepository = ResultRepository(database)

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(@ApplicationContext context: Context): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    @Singleton
    @Provides
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context,
        googleSignInOptions: GoogleSignInOptions,
    ): GoogleSignInClient =
        GoogleSignIn.getClient(context, googleSignInOptions)
}
