package com.example.androidproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.ChangePasswordRequest
import com.example.androidproject.model.ChangePasswordResponse
import com.example.androidproject.viewmodel.ForgotPassViewModel.ForgotPasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val apiService: ApiService) : ViewModel() {
    private val _ChangePassState = MutableStateFlow<ChangePassState>(ChangePassState.Idle)
    val changePassState = _ChangePassState.asStateFlow()

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _ChangePassState.value = ChangePassState.Loading
            try {
                val response = apiService.updatePass(ChangePasswordRequest(oldPassword, newPassword))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _ChangePassState.value = ChangePassState.Success(response.body())
                    } else {
                        _ChangePassState.value = ChangePassState.Error("Response body is null")
                    }
                } else {
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _ChangePassState.value = ChangePassState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _ChangePassState.value = ChangePassState.Error(e.message.toString())
            }
        }
    }

    fun resetState() {
        _ChangePassState.value = ChangePassState.Idle
    }

    sealed class ChangePassState {
        object Idle : ChangePassState()
        object Loading : ChangePassState()
        data class Success(val data: ChangePasswordResponse?) : ChangePassState()
        data class Error(val message: String) : ChangePassState()
    }
}