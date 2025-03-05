package com.example.androidproject.viewmodel.factories.client_profile

import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.client_profile.UpdateClientProfilePictureViewModel

class UpdateClientProfilePictureViewModelFactory (private val apiService: ApiService):
    ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateClientProfilePictureViewModel::class.java)) {
            return UpdateClientProfilePictureViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}