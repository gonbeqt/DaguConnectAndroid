package com.example.androidproject.viewmodel.factories.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.notifications.GetNotificationViewModel

class GetNotificationViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetNotificationViewModel::class.java)) {
            return  GetNotificationViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}