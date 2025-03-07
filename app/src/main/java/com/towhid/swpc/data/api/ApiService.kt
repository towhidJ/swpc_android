package com.towhid.swpc.data.api

import com.towhid.swpc.data.models.LoginRequest
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.data.models.RefreshTokenRequest
import com.towhid.swpc.data.models.WeatherForecast
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/Auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse> // Use Response for better error handling


    @POST("api/Auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): LoginResponse

    @GET("WeatherForecast")
    suspend fun getProtectedResource(@Header("Authorization") token: String): WeatherForecast
}