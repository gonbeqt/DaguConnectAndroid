package com.example.androidproject.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegisterViewModel(private val apiService: ApiService):ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun register(firstName:String, lastName: String, username: String, email:String, age: Int, isClient: Boolean, password:String, confirmPassword:String) {
        viewModelScope.launch { // Launch a coroutine to make the API call
            _registerState.value = RegisterState.Loading
            try {
                val response = apiService.register(RegisterRequest(firstName, lastName, username, email, age, isClient, password, confirmPassword)) // API call
                if (response.isSuccessful) {
                    _registerState.value = RegisterState.Success(response.body())
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        Log.d("LoginViewModel", "Login state:: ${_registerState.value}")
                    }
                } else {
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message")
                    _registerState.value = RegisterState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("Error Register View Model", "Unknown error: $e")
            }
        }
    }
    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        data class Success(val data: RegisterResponse?): RegisterState()
        data class Error(val message: String): RegisterState()
    }
}