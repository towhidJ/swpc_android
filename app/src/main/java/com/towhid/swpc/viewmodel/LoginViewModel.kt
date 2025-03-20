package com.towhid.swpc.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.towhid.swpc.data.api.ApiService
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.models.LoginRequest
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.data.models.LogoutResponse
import com.towhid.swpc.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService: ApiService = RetrofitClient.apiService;
    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")


    var isLoading by mutableStateOf(false)
    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

//    fun onUsernameChange(newUsername: String) {
//        username = newUsername
//    }
//
//    fun onPasswordChange(newPassword: String) {
//        password = newPassword
//    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = apiService.login(LoginRequest(username, password))
                TokenManager.saveTokens(getApplication(), response)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
            isLoading = false
        }
    }

    fun logout(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Call the logout API
                val response: Response<LogoutResponse> =
                    RetrofitClient.apiService.logout("Bearer $token")
                Log.d("resLogout", response.toString())
                if (response.isSuccessful) {
                    // Logout successful
                    TokenManager.clearTokens(getApplication())
                    onSuccess()
                } else {
                    // Handle API error
                    onError("Logout failed: ${response.message()}")
                }
            } catch (e: Exception) {
                // Handle network or other errors
                onError("Logout failed: ${e.message}")
            }
        }
    }


}