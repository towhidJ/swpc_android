package com.towhid.swpc.data.models

data class AuthRequest(
    val username:String,
    val password:String
)


data class LoginRequest(val email: String, val password: String)
data class RefreshTokenRequest(val refreshToken: String)
data class RefreshTokenResponse(val accessToken: String)
data class WeatherForecast(
    val date: String,
    val temperatureC: Int,
    val temperatureF: Int,
    val summary: String
)
data class LoginResponse(
    val userId:Int,
    val accessToken: String,
    val user: User?,
    val role: String?
)

data class User(
    val username: String,
    val name: String,


)

data class LoginFailureResponse(
    val status: String,
    val message: String
)