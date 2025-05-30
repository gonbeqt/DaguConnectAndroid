package com.example.androidproject.viewmodel.client_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.ClientProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetClientProfileViewModel(private val apiService: ApiService) : ViewModel() {
    private val _getProfileState = MutableStateFlow<ClientProfileState>(ClientProfileState.Idle)
    val getProfileState: MutableStateFlow<ClientProfileState> = _getProfileState

    fun getClientProfile() {
        _getProfileState.value = ClientProfileState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getClientProfile()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _getProfileState.value = ClientProfileState.Success(body)
                    }
                    } else {
                        _getProfileState.value = ClientProfileState.Error(response.message())

                    }
                } catch (e: Exception) {
                    _getProfileState.value = ClientProfileState.Error(e.localizedMessage ?: "Unknown error")
                }
        }
    }

    fun resetState(){
        _getProfileState.value = ClientProfileState.Idle
    }

    sealed class ClientProfileState {
        object Loading : ClientProfileState()
        object Idle : ClientProfileState()
        data class Success(val data: ClientProfile) : ClientProfileState()
        data class Error(val message: String) : ClientProfileState()

    }
}