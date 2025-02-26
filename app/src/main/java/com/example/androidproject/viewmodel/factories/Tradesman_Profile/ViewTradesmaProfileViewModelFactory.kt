package com.example.androidproject.viewmodel.factories.Tradesman_Profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel

class ViewTradesmaProfileViewModelFactory(private val apiService: ApiService, private val context: Context): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewTradesmanProfileViewModel::class.java)) {
            return ViewTradesmanProfileViewModel(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}