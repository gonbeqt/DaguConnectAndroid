package com.example.androidproject.viewmodel.factories.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.bookings.UpdateBookingClientViewModel

class UpdateBookingClientViewModelFactory(private val apiService : ApiService) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateBookingClientViewModel::class.java)) {
            return UpdateBookingClientViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}