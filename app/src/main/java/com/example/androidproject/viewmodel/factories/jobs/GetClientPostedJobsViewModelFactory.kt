package com.example.androidproject.viewmodel.factories.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.GetClientPostedJobsViewModel

class GetClientPostedJobsViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetClientPostedJobsViewModel::class.java)) {
            return  GetClientPostedJobsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}