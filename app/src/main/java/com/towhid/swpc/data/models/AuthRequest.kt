package com.towhid.swpc.data.models

data class AuthRequest(
    val username:String,
    val password:String
)


data class LoginRequest(val username: String, val password: String)
data class RefreshTokenRequest(val refreshToken: String)
data class WeatherForecast(
    val date: String,
    val temperatureC: Int,
    val temperatureF: Int,
    val summary: String
)
data class LoginResponse(
    val token: String,
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

data class User(
    val username: String,
    val role: String
)

data class LoginFailureResponse(
    val status: String,
    val message: String
)