package com.example.androidproject.viewmodel.factories.jobs

import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.PutJobViewModel

class PutJobViewModelFactory (private val apiService: ApiService):
    ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PutJobViewModel::class.java)) {
            return PutJobViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}