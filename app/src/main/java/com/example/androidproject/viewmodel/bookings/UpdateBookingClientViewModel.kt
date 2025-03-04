package com.example.androidproject.viewmodel.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.JsonErrorParser
import com.example.androidproject.model.client.ClientWorkStatusRequest
import com.example.androidproject.model.client.ClientWorkStatusResponse
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel.UpdateWorkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateBookingClientViewModel(private val apiService: ApiService): ViewModel() {
    private val _clientWorkStatusState = MutableStateFlow<UpdateClientWorkStatus>(UpdateClientWorkStatus.Idle)
    val clientWorkStatusState = _clientWorkStatusState.asStateFlow()

    fun updateClientWorkStatus(book_status: String, cancel_reason: String, bookingId: Int) {
        viewModelScope.launch {
            _clientWorkStatusState.value = UpdateClientWorkStatus.Loading
            try {
                val response = apiService.updateBookingClientStatus(ClientWorkStatusRequest(book_status,cancel_reason),bookingId)
                    if (response.isSuccessful){
                        val body = response.body()
                            if(body!=null){
                                _clientWorkStatusState.value = UpdateClientWorkStatus.Success(body,book_status)
                                }else{
                                    _clientWorkStatusState.value = UpdateClientWorkStatus.Error("Failed to update work status.")
                    }
                }else{
                    // Handle error response
                    val errorJson = response.errorBody()?.string()
                    val errorMessage = JsonErrorParser.extractField(errorJson, "message") ?: "Unknown error"
                    _clientWorkStatusState.value = UpdateClientWorkStatus.Error(errorMessage)
                }
            } catch (e: Exception) {
                _clientWorkStatusState.value = UpdateClientWorkStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _clientWorkStatusState.value = UpdateClientWorkStatus.Idle // Add an idle state
    }
    sealed class UpdateClientWorkStatus {
        object Idle : UpdateClientWorkStatus()
        object Loading : UpdateClientWorkStatus()
        data class Success(val data: ClientWorkStatusResponse?, val status: String) : UpdateClientWorkStatus()
        data class Error(val message: String) : UpdateClientWorkStatus()
    }


}