package com.towhid.swpc.data.api

import com.towhid.swpc.data.models.DeviceResponse
import com.towhid.swpc.data.models.LoginRequest
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.data.models.RefreshTokenRequest
import com.towhid.swpc.data.models.RefreshTokenResponse
import com.towhid.swpc.data.models.WeatherForecast
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/Auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse // Use Response for better error handling

    @GET("/device/get")
    suspend fun getProtectedResource(@Header("Authorization") token: String): WeatherForecast

    @GET("/device/get")
    fun getDevice(@Header("Authorization") token: String,@Query("userId") userId: Int): Call<DeviceResponse>
}