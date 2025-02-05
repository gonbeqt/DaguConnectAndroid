package com.example.androidproject.viewmodel.factories.chats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.chats.GetChatViewModel

class GetChatViewModelFactory(private val apiService: ApiService, private val context: Context):ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetChatViewModel::class.java)) {
            return  GetChatViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}