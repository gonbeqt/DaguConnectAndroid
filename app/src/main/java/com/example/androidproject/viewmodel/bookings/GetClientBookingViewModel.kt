package com.example.androidproject.viewmodel.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.viewmodel.bookings.paginate.GetClientBookingPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


class GetClientBookingViewModel(private val apiService: ApiService) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetClientBookingPagingSource?>(null)


    val ClientBookingPagingData: Flow<PagingData<GetClientsBooking>> =
        Pager(

            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GetClientBookingPagingSource(apiService).also { _pagingSource.value = it }
            }
        ).flow.cachedIn(viewModelScope)



}