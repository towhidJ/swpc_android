package com.towhid.swpc.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.towhid.swpc.data.api.ApiService
import com.towhid.swpc.data.api.RetrofitClient
import com.towhid.swpc.data.models.LoginRequest
import com.towhid.swpc.data.models.LoginResponse
import com.towhid.swpc.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


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


}