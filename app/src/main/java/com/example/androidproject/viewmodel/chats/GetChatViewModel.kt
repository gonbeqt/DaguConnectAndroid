package com.example.androidproject.viewmodel.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Chats
import com.example.androidproject.model.GetChats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GetChatViewModel(private val apiService: ApiService) : ViewModel() {
    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatState: MutableStateFlow<ChatState> = _chatState

    fun getChats() {
        _chatState.value = ChatState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getChat()
                if (response.isSuccessful) {
                    val body = response.body()
                    val chats = body?.chats
                    if (body != null) {
                        _chatState.value = chats?.let { ChatState.Success(it) }!!
                    }
                } else {
                    _chatState.value = ChatState.Error(response.message())
                }
            } catch (e: Exception) {
                _chatState.value = ChatState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    sealed class ChatState {
        object Idle : ChatState()
        object Loading : ChatState()
        data class Success(val data: List<Chats>) : ChatState()
        data class Error(val message: String) : ChatState()
    }
}