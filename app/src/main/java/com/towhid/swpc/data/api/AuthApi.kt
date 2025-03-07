package com.towhid.swpc.data.api

import com.towhid.swpc.data.models.AuthRequest
import com.towhid.swpc.data.models.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<AuthResponse>
}