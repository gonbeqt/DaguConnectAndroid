package com.example.androidproject.viewmodel.factories.jobs

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.jobs.PostJobViewModel

class PostJobViewModelFactory(private val apiService: ApiService, private val context: Context):
    ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostJobViewModel::class.java)) {
            return PostJobViewModel(apiService, context) as T
        }
            throw IllegalArgumentException("Unknown ViewModel class")
    }

}