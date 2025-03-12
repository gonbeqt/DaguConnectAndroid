package com.example.androidproject.viewmodel.client_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.UpdateAddress
import com.example.androidproject.model.UpdateAddressResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdateClientProfileAddressViewModel(private val apiService: ApiService): ViewModel() {
    private val _updateClientProfileState = MutableStateFlow<UpdateClientProfileAddressState>(
        UpdateClientProfileAddressState.Idle)
    val updateClientProfileState: StateFlow<UpdateClientProfileAddressState> = _updateClientProfileState

    fun updateClientProfile(address: String,phoneNumber : String) {
        viewModelScope.launch {
            _updateClientProfileState.value = UpdateClientProfileAddressState.Loading
            val data  = UpdateAddress(address,phoneNumber)
            val put = apiService.updateClientDetails(data)
            try {
                if (put.isSuccessful) {
                    _updateClientProfileState.value = UpdateClientProfileAddressState.Success(put)
                } else {
                    _updateClientProfileState.value = UpdateClientProfileAddressState.Error(put.message())
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
        data object Idle: UpdateClientProfileAddressState()
        data object Loading: UpdateClientProfileAddressState()
        data class Success(val data: Response<UpdateAddressResponse>): UpdateClientProfileAddressState()
        data class Error(val message: String): UpdateClientProfileAddressState()

    }
}