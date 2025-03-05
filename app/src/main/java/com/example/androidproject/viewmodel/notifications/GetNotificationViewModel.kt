package com.example.androidproject.viewmodel.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.androidproject.model.Notification
import com.example.androidproject.viewmodel.notifications.paginate.GetNotificationPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService

class GetNotificationViewModel(private val apiService: ApiService):ViewModel() {
    private val refreshTrigger = MutableStateFlow(Unit)
    private val _pagingSource = MutableStateFlow<GetNotificationPagingSource?>(null)
    val getNotificationPagingData: Flow<PagingData<Notification>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetNotificationPagingSource(apiService).also { _pagingSource.value =it } }
        ).flow.cachedIn(viewModelScope)

    fun refreshNotification() {
        refreshTrigger.value = Unit
    }
}