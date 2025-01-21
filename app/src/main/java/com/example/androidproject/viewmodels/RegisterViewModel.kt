package com.example.androidproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.RegisterRequest
import com.example.androidproject.model.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RegisterViewModel(private val apiService: ApiService):ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    fun register(registerRequest: RegisterRequest) {
        // Launch a coroutine to make the API call
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = apiService.register(registerRequest) // API call
                _registerState.value = RegisterState.Success(response.body())
                if (!response.isSuccessful) {
                    _registerState.value = RegisterState.Error("Invalid credentials or server error")
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