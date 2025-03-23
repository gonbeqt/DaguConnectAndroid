package com.example.androidproject.viewmodel.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Chats
import com.example.androidproject.viewmodel.chats.paginate.GetChatPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetChatViewModel(private val apiService: ApiService) : ViewModel() {
    private val _pagingSource = MutableStateFlow<GetChatPagingSource?>(null)

    val getChatsPagingData: Flow<PagingData<Chats>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetChatPagingSource(apiService).also { _pagingSource.value = it }
            }
        ).flow.cachedIn(viewModelScope)

    fun refreshChats() {
        _pagingSource.value?.invalidate()
    }
}