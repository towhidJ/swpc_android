package com.towhid.swpc.data.models

data class AuthResponse(
    val token: String,
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

data class User(
    val userId: Int,
    val userName: String,
    val roleName: String,
    val roleId: Int,
    val isActive: Boolean
)