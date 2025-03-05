package com.example.androidproject.viewmodel.factories.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel


class UpdateWorkStatusViewModelFactory(private val apiService: ApiService, private val context : Context) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateBookingTradesmanViewModel::class.java)) {
            return UpdateBookingTradesmanViewModel(apiService,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}