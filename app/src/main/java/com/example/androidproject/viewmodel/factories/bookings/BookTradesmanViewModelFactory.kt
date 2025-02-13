package com.example.androidproject.viewmodel.factories.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel


class BookTradesmanViewModelFactory(private val apiService: ApiService, context : Context): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooktradesmanViewModel::class.java)) {
            return BooktradesmanViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}