package com.example.androidproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.ForgotPasswordRequest
import com.example.androidproject.model.ForgotPasswordResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPassViewModel(private val apiService: ApiService):ViewModel() {
    private val _ForgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState = _ForgotPasswordState.asStateFlow()

    fun forgotPassword(email: String) {
        viewModelScope.launch{
            _ForgotPasswordState.value = ForgotPasswordState.Loading
            try {
                val response = apiService.forgotPass(ForgotPasswordRequest(email))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body!= null) {
                        _ForgotPasswordState.value = ForgotPasswordState.Success(body)
                    }else{
                        _ForgotPasswordState.value = ForgotPasswordState.Error("Response body is null")
                    }
                } else {
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _ForgotPasswordState.value = ForgotPasswordState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _ForgotPasswordState.value = ForgotPasswordState.Error(e.message?: "An error occurred")
            }
        }
    }
    fun resetState(){
        _ForgotPasswordState.value = ForgotPasswordState.Idle

    }
    sealed class ForgotPasswordState {
        object Idle : ForgotPasswordState()
        object Loading : ForgotPasswordState()
        data class Success(val data: ForgotPasswordResponse?) : ForgotPasswordState()
        data class Error(val message: String) : ForgotPasswordState()
    }
}