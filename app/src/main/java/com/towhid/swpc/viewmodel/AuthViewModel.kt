package com.towhid.swpc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.towhid.swpc.data.models.AuthResponse
import com.towhid.swpc.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val response = repository.login(username, password)
            if (response.isSuccessful) {
                response.body()?.let {
                    repository.saveTokens(it.accessToken, it.refreshToken)
                    _authState.value = AuthState.Success(it)
                }
            } else {
                _authState.value = AuthState.Error("Authentication Failed")
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            val response = repository.refreshAccessToken()
            if (response != null && response.isSuccessful) {
                response.body()?.let {
                    repository.saveTokens(it.accessToken, it.refreshToken)
                }
            } else {
                _authState.value = AuthState.Unauthorized
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.LoggedOut
        }
    }
}

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthorized : AuthState()
    object LoggedOut : AuthState()
}