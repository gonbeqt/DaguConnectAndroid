package com.example.androidproject.viewmodel.factories.resumes

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel

class ViewResumeViewModelFactory (private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewResumeViewModel::class.java)) {
            return ViewResumeViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}