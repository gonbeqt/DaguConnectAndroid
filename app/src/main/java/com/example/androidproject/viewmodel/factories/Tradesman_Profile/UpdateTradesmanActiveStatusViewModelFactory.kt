package com.example.androidproject.viewmodel.factories.Tradesman_Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanActiveStatusViewModel

class UpdateTradesmanActiveStatusViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateTradesmanActiveStatusViewModel::class.java)) {
            return UpdateTradesmanActiveStatusViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}