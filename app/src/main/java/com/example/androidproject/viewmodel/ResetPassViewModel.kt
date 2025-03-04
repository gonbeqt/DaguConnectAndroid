package com.example.androidproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.ResetPasswordRequest
import com.example.androidproject.model.ResetPasswordResponse
import com.example.androidproject.viewmodel.ForgotPassViewModel.ForgotPasswordState
import com.example.androidproject.viewmodel.LoginViewModel.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResetPassViewModel(private val apiService: ApiService):ViewModel() {
    private val _resetPassState = MutableStateFlow<ResetPassState>(ResetPassState.Idle)
    val resetPassState = _resetPassState.asStateFlow()

    fun resetPassword(token: Int, newPassword: String){
        viewModelScope.launch {
            _resetPassState.value = ResetPassState.Loading
            try{
                val response = apiService.resetPass(ResetPasswordRequest(token, newPassword))
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null){
                        _resetPassState.value = ResetPassState.Success(body)
                    }else{
                        // Handle error response
                        val errorJson = response.errorBody()?.string()
                        val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                        _resetPassState.value = ResetPassState.Error(errorMessage)
                    }
                }
            }catch (e: Exception){
                _resetPassState.value = ResetPassState.Error(e.message?: "An error occurred")
            }
        }
    }
    fun resetState() {
        _resetPassState.value = ResetPassState.Idle
    }

    sealed class ResetPassState{
        object Idle : ResetPassState()
        object Loading : ResetPassState()
        data class Success(val data: ResetPasswordResponse?) : ResetPassState()
        data class Error(val message: String) : ResetPassState()

    }
}