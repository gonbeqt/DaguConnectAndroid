package com.example.androidproject.viewmodel.factories.resumes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel


class GetResumesViewModelFactory(private val apiService: ApiService,private val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetResumesViewModel::class.java)) {
            return  GetResumesViewModel(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}