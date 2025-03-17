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
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.models.AuthResponse
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
    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    private val _weatherForecasts = MutableStateFlow<List<WeatherForecast>?>(null)
    val weatherForecasts: StateFlow<List<WeatherForecast>?> = _weatherForecasts



    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    init {
        checkLoginState() // Check for existing tokens on ViewModel creation
    }

    private fun checkLoginState() {
        viewModelScope.launch {
            val token = TokenManager.getAccessToken(getApplication())
            Log.e("accToken", token.toString())
            if (token != null && !isTokenExpired(token)) {
                try {
                    val userId = TokenManager.getUserId(getApplication())
                    val role = TokenManager.getUserRole(getApplication())
                    val user = TokenManager.getUserData(getApplication())

                    _loginResult.value = userId?.let { LoginResponse(it, token, user, role) }
                } catch (e: IOException) {
                    Log.e("RefreshToken", "Network error: ${e.message}", e)
                    logout()
                } catch (e: HttpException) {
                    Log.e("RefreshToken", "HTTP error: ${e.code()} - ${e.message()}", e)
                    logout()
                } catch (e: Exception) {
                    Log.e("RefreshToken", "Unexpected error: ${e.message}", e)
                    logout()
                }
            } else {
                logout()
            }
        }
    }
//    fun login(username: String, password: String) {
//        viewModelScope.launch {
//            try {
//                val response: Response<LoginResponse> = RetrofitClient.apiService.login(LoginRequest(username, password))
//
//                if (response.isSuccessful) {
//                    val loginResponse = response.body()
//                    if (loginResponse != null) {
//                        TokenManager.saveTokens(getApplication(), loginResponse)
//                        _loginResult.value = loginResponse
//                    } else {
//                        _errorMessage.value = "Login successful, but response body was empty."
//                    }
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    if (errorBody != null) {
//                        try {
//                            val failureResponse = Gson().fromJson(errorBody, LoginFailureResponse::class.java)
//                            _errorMessage.value = failureResponse.message
//                        } catch (e: Exception) {
//                            _errorMessage.value = "Login failed, but could not parse error response."
//                        }
//                    } else {
//                        _errorMessage.value = "Login failed with code: ${response.code()}"
//                    }
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Login failed: ${e.message}"
//            }
//        }
//    }
//

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
    fun clearLoginResult() {
        _loginResult.value = null // Reset state after successful login
    }

    fun logout(){
        viewModelScope.launch {
            TokenManager.clearTokens(getApplication())
            _loginResult.value = null;
        }
    }
}
