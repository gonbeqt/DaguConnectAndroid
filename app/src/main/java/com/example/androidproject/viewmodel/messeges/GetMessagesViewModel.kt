package com.example.androidproject.viewmodel.messeges

import GetMessagesPagingSource
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetMessages
import com.example.androidproject.viewmodel.jobs.paginate.GetJobsPagingSource
import kotlinx.coroutines.flow.Flow

class GetMessagesViewModel(private val apiService: ApiService, private val chatId: Int):ViewModel() {
    val messegesPagingData: Flow<PagingData<GetMessages>> = Pager(
        config = PagingConfig(
            pageSize = 10, // Keep it consistent with API
            initialLoadSize = 10, // Prevents large first requests
            prefetchDistance = 2, // Reduces prefetching aggressiveness
            enablePlaceholders = false // Helps prevent extra loads
        ),
        pagingSourceFactory = { GetMessagesPagingSource(apiService, chatId) }
    ).flow.cachedIn(viewModelScope)
}