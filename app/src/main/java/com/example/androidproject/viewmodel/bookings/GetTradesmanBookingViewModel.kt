package com.example.androidproject.viewmodel.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.GetTradesmanBooking
import com.example.androidproject.viewmodel.bookings.paginate.GetTradesmanBookingPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetTradesmanBookingViewModel(private val apiService: ApiService) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetTradesmanBookingPagingSource?>(null)

    val TradesmanBookingPagingData: Flow<PagingData<GetTradesmanBooking>> =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetTradesmanBookingPagingSource(apiService).also { _pagingSource.value =it}
            }
        ).flow.cachedIn(viewModelScope)
}