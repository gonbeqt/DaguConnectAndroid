package com.example.androidproject.viewmodel.factories.messeges

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.factories.notification.GetMessageViewModelFactory

class GetMessageViewModelFactory(private val apiService: ApiService, private val context: Context): ViewModelProvider.Factory  {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetMessageViewModelFactory::class.java)) {
            return  GetMessageViewModelFactory(apiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}