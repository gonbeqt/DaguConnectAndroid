package com.example.androidproject.viewmodel.factories.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.DeleteJobViewModel

class DeleteJobViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteJobViewModel::class.java)) {
            return  DeleteJobViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}