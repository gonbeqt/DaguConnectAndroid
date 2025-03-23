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
import com.example.androidproject.model.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.asStateFlow

class GetMessagesViewModel(
    private val apiService: ApiService,
    private val chatId: Int,
    private val receiverId: Int
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
                    GetMessagesPagingSource(apiService, chatId, receiverId).also {
                        lastPagingSource = it
                    }
                }
            ).flow.cachedIn(viewModelScope)
        }


    fun refreshMessages() {
        refreshTrigger.value = Unit
        Log.d("GetMessagesViewModel", "Refreshing messages...")

        lastPagingSource?.invalidate()
    }
}
