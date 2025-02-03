package com.example.androidproject.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.LoginRequest
import com.example.androidproject.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: ApiService, private val context: Context):ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow() // Exposing the state as StateFlow

    init {
        AccountManager.init(context)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginState.value = LoginState.Success(response.body())
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        val user = loginResponse.user
                        AccountManager.saveAccount(user)
                        loginResponse.token?.let { TokenManager.saveToken(it) }
                        Log.d("LoginViewModel", "Token: ${loginResponse.token}")
                        Log.d("LoginViewModel", "Login state:: ${_loginState.value}")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message")
                    _loginState.value = LoginState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("Error Login View Model", "Unknown error: $e")
            }
        }


    }
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val data: LoginResponse?) : LoginState()
        data class Error(val message: String) : LoginState()
    }
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}