package com.example.androidproject.viewmodel.factories.job_application

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.job_application.PostJobApplicationViewModel

class PostJobApplicationViewModelFactory(private val apiService: ApiService, private val context: Context): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostJobApplicationViewModel::class.java)) {
            return  PostJobApplicationViewModel(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}