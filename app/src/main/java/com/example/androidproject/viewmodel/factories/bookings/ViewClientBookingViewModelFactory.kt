package com.example.androidproject.viewmodel.factories.bookings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel

class ViewClientBookingViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewClientBookingViewModel::class.java)) {
            return ViewClientBookingViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}