package io.github.skincanorg.skincan.data.api

import io.github.skincanorg.skincan.data.model.ApiResponse
import io.github.skincanorg.skincan.data.model.LoginResponse
import io.github.skincanorg.skincan.data.model.MiniUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun register(@Body user: MiniUser): Response<ApiResponse> // TODO: Check response

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body user: MiniUser): Response<LoginResponse>
}
