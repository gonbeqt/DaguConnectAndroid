package com.example.androidproject.viewmodel.client_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.ClientProfile
import com.example.androidproject.viewmodel.chats.GetChatViewModel.ChatState
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

    sealed class ClientProfileState {
        object Idle : ClientProfileState()
        object Loading : ClientProfileState()
        data class Success(val data: ClientProfile) : ClientProfileState()
        data class Error(val message: String) : ClientProfileState()

    }
}