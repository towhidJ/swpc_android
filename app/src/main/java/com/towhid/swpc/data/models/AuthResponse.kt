package com.towhid.swpc.data.models

data class AuthResponse(
    val token: String,
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

data class LogoutResponse(
    val message: String
)
