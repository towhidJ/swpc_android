package com.towhid.swpc.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.IOException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.towhid.swpc.AuthState
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.models.LoginFailureResponse
import com.towhid.swpc.data.models.LoginRequest
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.data.models.RefreshTokenRequest
import com.towhid.swpc.data.models.WeatherForecast
import com.towhid.swpc.util.TokenManager
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.util.Base64

class AuthViewModel(application: Application) : AndroidViewModel(application) {


    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    fun checkAuthState() {
        // Logic to check authentication state
        _authState.value = AuthState.Authenticated // or Unauthenticated
    }
    // Function to decode JWT and check expiration
    private fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size < 3) return true

            val payload = String(Base64.getUrlDecoder().decode(parts[1]))
            val json = JSONObject(payload)
            val exp = json.getLong("exp")

            val currentTime = System.currentTimeMillis() / 1000
            exp < currentTime
        } catch (e: Exception) {
            Log.e("TokenCheck", "Error checking token expiry: ${e.message}", e)
            true // Assume expired if there's an error
        }
    }

}
