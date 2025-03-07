package com.towhid.swpc.data.repository

import com.towhid.swpc.data.api.AuthApi
import com.towhid.swpc.data.models.AuthRequest
import com.towhid.swpc.data.models.AuthResponse
import com.towhid.swpc.util.DataStoreManager
import retrofit2.Response

class AuthRepository(private val api: AuthApi, private val dataStore: DataStoreManager) {

    suspend fun login(username: String, password: String): Response<AuthResponse> {
        return api.login(AuthRequest(username, password))
    }

    suspend fun refreshAccessToken(): Response<AuthResponse>? {
        val refreshToken = dataStore.getRefreshToken() ?: return null
        return api.refreshToken("Bearer $refreshToken")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.saveTokens(accessToken, refreshToken)
    }

    suspend fun logout() {
        dataStore.clearTokens()
    }
}