package com.example.androidproject.viewmodel.factories.resumes


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Resumes.SubmitResumeViewModel


class SubmitResumeViewModelFactory(private val apiService: ApiService, context : Context): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubmitResumeViewModel::class.java)) {
            return SubmitResumeViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}