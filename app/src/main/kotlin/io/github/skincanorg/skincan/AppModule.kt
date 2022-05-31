package io.github.skincanorg.skincan

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.skincanorg.skincan.data.api.AuthService
import io.github.skincanorg.skincan.data.preference.PreferencesHelper
import io.github.skincanorg.skincan.lib.Util
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // --- [ Start of qualifier defines ]

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggedInRetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggedInOkHttpClient

    // AuthRetrofitClient only used for login and register
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofitClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthOkHttpClient

    // --- [ End of qualifier defines ]

    @Singleton
    @Provides
    fun providesPreferences(@ApplicationContext context: Context) = PreferencesHelper(context)

    @Singleton
    @Provides
    @AuthOkHttpClient
    fun providesAuthOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    @AuthRetrofitClient
    fun providesAuthRetroFit(@AuthOkHttpClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(Util.API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesAuthApiService(@AuthRetrofitClient retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}
