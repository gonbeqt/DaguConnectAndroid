package com.example.androidproject.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.ReportConcernViewModel

class ReportConcernViewModelFactory(private val apiService : ApiService):ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportConcernViewModel::class.java)) {
            return ReportConcernViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}