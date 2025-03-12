package com.example.androidproject.viewmodel.messeges

import GetMessagesPagingSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.model.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GetMessagesViewModel(
    private val apiService: ApiService,
    private val chatId: Int
) : ViewModel() {

    private val _newMessages = MutableStateFlow<List<Conversation>>(emptyList())
    val newMessages = _newMessages.asStateFlow()

    // Create a trigger for refreshing the Pager
    private val refreshTrigger = MutableStateFlow(Unit)

    val messagesPagingData: Flow<PagingData<Conversation>> =
        refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 20,
                    prefetchDistance = 2,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { GetMessagesPagingSource(apiService, chatId) }
            ).flow.cachedIn(viewModelScope)
        }

    fun addLocalMessage(message: String, receiverId: Int, chatId: Int) {
        val currentUserId = AccountManager.getAccount()?.id ?: return

        val newMessage = Conversation(
            id = -1, // Or a temporary local ID
            userId = currentUserId,
            receiverId = receiverId,
            chatId = chatId,
            message = message,
            isRead = 1, // Already read because it's sent by me
            createdAt = "" // Or LocalDateTime.now().toString()
        )

        _newMessages.value = _newMessages.value + newMessage
    }


    // Call this function to refresh PagingData
    fun refreshMessages() {
        refreshTrigger.value = Unit
    }
}
