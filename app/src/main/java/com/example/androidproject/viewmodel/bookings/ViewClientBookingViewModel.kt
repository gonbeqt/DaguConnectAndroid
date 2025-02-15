package com.example.androidproject.viewmodel.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.ViewClientBooking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewClientBookingViewModel(private val  apiService: ApiService, private val context: Context):ViewModel() {
    private val _viewClientBooking = MutableStateFlow<ViewClientBookings>(ViewClientBookings.Idle)
    val viewClientBookingState = _viewClientBooking.asStateFlow()

    fun viewClientBooking(resumeId: Int){
        viewModelScope.launch {
            _viewClientBooking.value = ViewClientBookings.Loading
            try {
                val response  = apiService.getCleintBookingById(resumeId)
                val body = response.body()
                if (response.isSuccessful && body!= null){
                    _viewClientBooking.value = ViewClientBookings.Success(body)
                }else{
                    _viewClientBooking.value = ViewClientBookings.Error("Failed to retrieve data")
                }

            }catch (e: Exception){
                _viewClientBooking.value = ViewClientBookings.Error("An error occurred")
            }
        }

    }

    sealed class ViewClientBookings{
        object Idle : ViewClientBookings()
        object Loading : ViewClientBookings()
        data class Success(val data: ViewClientBooking) : ViewClientBookings()
        data class Error(val message: String) : ViewClientBookings()

    }
}