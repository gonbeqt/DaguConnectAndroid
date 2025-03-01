package com.example.androidproject.viewmodel.bookings

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.GetTradesmanBooking
import com.example.androidproject.viewmodel.bookings.paginate.GetClientBookingPagingSource
import com.example.androidproject.viewmodel.bookings.paginate.GetTradesmanBookingPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetTradesmanBookingViewModel(private val apiService: ApiService) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetTradesmanBookingPagingSource?>(null)
    private val _dismissedBookings = mutableStateOf(setOf<Int>())
    val dismissedBookings: State<Set<Int>> = _dismissedBookings

    val TradesmanBookingPagingData: Flow<PagingData<GetTradesmanBooking>> = Pager(

        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10,
            prefetchDistance = 2,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            GetTradesmanBookingPagingSource(apiService).also { _pagingSource.value = it }
        }
    ).flow.cachedIn(viewModelScope)



    // Function to invalidate the PagingSource
    fun invalidatePagingSource() {
        _pagingSource.value?.invalidate()
    }

    fun dismissBooking(BookingId: Int) {
        _dismissedBookings.value = _dismissedBookings.value + BookingId
        invalidatePagingSource() // Force reload after dismissal
    }
}