package com.example.androidproject.viewmodel.factories.jobs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel

class GetJobsViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetJobsViewModel::class.java)) {
            return  GetJobsViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}