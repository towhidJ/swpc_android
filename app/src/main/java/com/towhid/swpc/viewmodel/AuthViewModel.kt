package com.towhid.swpc.viewmodel

import android.app.Application
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
import retrofit2.HttpException
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    private val _weatherForecasts = MutableStateFlow<List<WeatherForecast>?>(null)
    val weatherForecasts: StateFlow<List<WeatherForecast>?> = _weatherForecasts

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = RetrofitClient.apiService.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        TokenManager.saveTokens(getApplication(), loginResponse.accessToken, loginResponse.refreshToken)
                        _loginResult.value = loginResponse
                    } else {
                        _errorMessage.value = "Login successful, but response body was empty."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        try {
                            val failureResponse = Gson().fromJson(errorBody, LoginFailureResponse::class.java)
                            _errorMessage.value = failureResponse.message
                        } catch (e: Exception) {
                            _errorMessage.value = "Login failed, but could not parse error response."
                        }
                    } else {
                        _errorMessage.value = "Login failed with code: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
            }
        }
    }

    fun getProtectedData() {
        viewModelScope.launch {
            try {
                val accessToken = TokenManager.getAccessToken(getApplication())
                if (accessToken == null) {
                    _errorMessage.value = "Not Logged in"
                    return@launch
                }

                val forecasts = RetrofitClient.apiService.getProtectedResource("Bearer $accessToken")
                _weatherForecasts.value = listOf(forecasts)
            } catch (e: HttpException) {
                if (e.code() == 401) { // Access token expired
                    refreshAccessToken()
                } else {
                    _errorMessage.value = "Failed to fetch data: ${e.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch data: ${e.message}"
            }
        }
    }
    fun refreshAccessToken() {
        viewModelScope.launch {
            try {
                val refreshToken = TokenManager.getRefreshToken(getApplication())
                if (refreshToken == null) {
                    _errorMessage.value = "Refresh token missing"
                    return@launch;
                }
                val response = RetrofitClient.apiService.refreshToken(RefreshTokenRequest(refreshToken))
                TokenManager.saveTokens(getApplication(), response.accessToken, response.refreshToken)
                getProtectedData() // Retry fetching data with the new access token
            } catch (e: Exception) {
                TokenManager.clearTokens(getApplication()) // clear tokens if refresh fails.
                _loginResult.value = null; // force login screen.
                _errorMessage.value = "Failed to refresh token: ${e.message}"
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            TokenManager.clearTokens(getApplication())

            _loginResult.value = null;
        }
    }
}
