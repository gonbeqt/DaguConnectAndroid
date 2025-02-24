package com.example.androidproject.viewmodel.factories.client_profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel

class GetClientProfileViewModelFactory(private val apiService: ApiService, private val context: Context):
    ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetClientProfileViewModel::class.java)) {
            return  GetClientProfileViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}