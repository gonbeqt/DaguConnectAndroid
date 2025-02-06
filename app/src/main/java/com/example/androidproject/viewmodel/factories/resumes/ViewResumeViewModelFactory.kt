package com.example.androidproject.viewmodel.factories.resumes

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel

class ViewResumeViewModelFactory (private val apiService: ApiService, private val context: Context): ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewResumeViewModel::class.java)) {
            return ViewResumeViewModel(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}