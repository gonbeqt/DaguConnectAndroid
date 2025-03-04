package com.example.androidproject.viewmodel.factories.job_application

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel

class ViewJobApplicationViewModelFactory (private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewJobApplicationViewModel::class.java)) {
            return ViewJobApplicationViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}