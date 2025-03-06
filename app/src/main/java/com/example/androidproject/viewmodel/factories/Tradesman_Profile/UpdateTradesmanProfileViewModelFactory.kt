package com.example.androidproject.viewmodel.factories.Tradesman_Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel


class UpdateTradesmanProfileViewModelFactory(private val apiService: ApiService):ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateTradesmanProfileViewModel::class.java)) {
            return UpdateTradesmanProfileViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}