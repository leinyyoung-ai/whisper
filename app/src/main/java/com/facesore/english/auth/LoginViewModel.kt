package com.facesore.english.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val authManager = AuthManager()

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if (authManager.getCurrentUser() != null) {
            _authState.value = AuthState.LoggedIn
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authManager.loginUser(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.LoggedIn
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authManager.registerUser(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.LoggedIn
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthState {
    object LoggedIn : AuthState()
    object LoggedOut : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
