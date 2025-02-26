package com.example.androidproject.viewmodel.factories.job_application.client

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel

class GetMyJobApplicantsViewModelFactory(private val apiService: ApiService, private val context: Context): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetMyJobApplicantsViewModel::class.java)) {
            return  GetMyJobApplicantsViewModel(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}