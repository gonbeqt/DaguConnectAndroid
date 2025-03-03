package com.example.androidproject.viewmodel.factories.messeges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.api.ApiService
import com.example.androidproject.viewmodel.messeges.GetMessagesViewModel

class GetMessageViewModelFactory(
    private val apiService: ApiService,
    private val chatId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetMessagesViewModel::class.java)) {
            return GetMessagesViewModel(apiService, chatId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}