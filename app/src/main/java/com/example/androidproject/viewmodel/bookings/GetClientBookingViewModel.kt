package com.example.androidproject.viewmodel.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.GetClientsBooking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetClientBookingViewModel(private val apiService: ApiService, private val context: Context) : ViewModel() {
    private val _clientbookingState = MutableStateFlow<GetClientBookings>(GetClientBookings.Idle)
    val clientbookingState = _clientbookingState.asStateFlow()

    fun getClientBookings() {
        viewModelScope.launch {
            _clientbookingState.value = GetClientBookings.Loading
            try {
                val token = TokenManager.getToken()
                val response = apiService.getClientBooking()
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        _clientbookingState.value = GetClientBookings.Success(body)
                    }
                } else {
                    _clientbookingState.value = GetClientBookings.Error(response.message())
                }
            } catch (e: Exception) {
                _clientbookingState.value = GetClientBookings.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    sealed class GetClientBookings{
        object Idle : GetClientBookings()
        object Loading : GetClientBookings()
        data class Success(val data: List<GetClientsBooking>) : GetClientBookings()
        data class Error(val message: String) : GetClientBookings()

    }
}