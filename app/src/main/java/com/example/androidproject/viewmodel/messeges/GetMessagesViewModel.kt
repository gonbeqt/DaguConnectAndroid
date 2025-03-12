package com.example.androidproject.viewmodel.messeges

import GetMessagesPagingSource
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class GetMessagesViewModel(
    private val apiService: ApiService,
    private val chatId: Int
) : ViewModel() {

    private val _newMessages = MutableStateFlow<List<Conversation>>(emptyList())
    val newMessages = _newMessages.asStateFlow()

    // Create a trigger for refreshing the Pager
    private val refreshTrigger = MutableStateFlow(Unit)

    private var lastPagingSource: GetMessagesPagingSource? = null

    val messagesPagingData: Flow<PagingData<Conversation>> =
        refreshTrigger.flatMapLatest {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 25,
                    prefetchDistance = 2,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    GetMessagesPagingSource(apiService, chatId).also {
                        lastPagingSource = it
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }

    fun addLocalMessage(message: String, receiverId: Int, chatId: Int) {
        val currentUserId = AccountManager.getAccount()?.id ?: return

        val newMessage = Conversation(
            id = -1, // Temporary local ID
            userId = currentUserId,
            receiverId = receiverId,
            chatId = chatId,
            message = message,
            isRead = 1, // Already read because it's sent by me
            createdAt = getCurrentTimestamp() // UTC timestamp
        )

        Log.d("GetMessagesViewModel", "Added local message: ${newMessage.message}, createdAt: ${newMessage.createdAt}")
        _newMessages.value = _newMessages.value + newMessage
    }

    @Synchronized
    fun getCurrentTimestamp(): String {
        val utcTime = System.currentTimeMillis()
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = utcTime
        }
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
            Log.d("GetMessagesViewModel", "Timestamp formatter timezone set to: ${timeZone.id}")
        }
        val timestamp = formatter.format(utcCalendar.time)
        Log.d("GetMessagesViewModel", "Generated timestamp: $timestamp, from millis: $utcTime, " +
                "formatted Date: ${utcCalendar.time}, device timezone: ${TimeZone.getDefault().id}")
        return timestamp
    }

    // Call this function to refresh PagingData
    fun refreshMessages() {
        refreshTrigger.value = Unit
        Log.d("GetMessagesViewModel", "Refreshing messages...")

        lastPagingSource?.invalidate() // Force reloading the PagingSource
    }
}
