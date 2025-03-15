package com.example.androidproject.viewmodel.client_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.UpdateAddress
import com.example.androidproject.model.UpdateAddressResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateClientProfileAddressViewModel(private val apiService: ApiService): ViewModel() {
    private val _updateClientProfileState = MutableStateFlow<UpdateClientProfileAddressState>(
        UpdateClientProfileAddressState.Idle)
    val updateClientProfileState: StateFlow<UpdateClientProfileAddressState> = _updateClientProfileState

    fun updateClientProfile(address: String,phoneNumber : String) {
        viewModelScope.launch {
            try {
                _updateClientProfileState.value = UpdateClientProfileAddressState.Loading

                val put = apiService.updateClientDetails(UpdateAddress(address,phoneNumber))
                if (put.isSuccessful) {
                    val body = put.body()
                    _updateClientProfileState.value = UpdateClientProfileAddressState.Success(body)
                } else {
                    val errorJson = put.errorBody()?.string()
                    println("Error response: $errorJson") // Debug log
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _updateClientProfileState.value = UpdateClientProfileAddressState.Error(errorMessage)
                }
            } catch (e: Exception) {
                _updateClientProfileState.value = UpdateClientProfileAddressState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _updateClientProfileState.value = UpdateClientProfileAddressState.Idle
    }

    sealed class UpdateClientProfileAddressState{
        object Idle: UpdateClientProfileAddressState()
        object Loading: UpdateClientProfileAddressState()
        data class Success(val data: UpdateAddressResponse?): UpdateClientProfileAddressState()
        data class Error(val message: String): UpdateClientProfileAddressState()

    }
}