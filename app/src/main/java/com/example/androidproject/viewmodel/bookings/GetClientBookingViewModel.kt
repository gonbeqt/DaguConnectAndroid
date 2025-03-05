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
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.viewmodel.bookings.paginate.GetClientBookingPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GetClientBookingViewModel(private val apiService: ApiService) : ViewModel() {

    private val _pagingSource = MutableStateFlow<GetClientBookingPagingSource?>(null)
    private val _dismissedBookings = mutableStateOf(setOf<Int>())
    val dismissedBookings: State<Set<Int>> = _dismissedBookings

    val ClientBookingPagingData: Flow<PagingData<GetClientsBooking>> = Pager(

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



    // Function to invalidate the PagingSource
    fun invalidatePagingSource() {
        _pagingSource.value?.invalidate()
    }

    fun dismissBooking(BookingId: Int) {
        _dismissedBookings.value = _dismissedBookings.value + BookingId
        invalidatePagingSource() // Force reload after dismissal
    }

   /* private val _clientbookingState = MutableStateFlow<GetClientBookings>(GetClientBookings.Idle)
    val clientbookingState = _clientbookingState.asStateFlow()

    fun getClientBookings() {
        viewModelScope.launch {
            _clientbookingState.value = GetClientBookings.Loading
            try {
                val response = apiService.getClientBooking()
                val body = response.body()
                if (response.isSuccessful) {
                    if (body != null) {
                        _clientbookingState.value = GetClientBookings.Success(body)
                    }
                } else {
                    _clientbookingState.value = GetClientBookings.Error(response.message())
                }
            } catch (e: Exception) {
                _clientbookingState.value = GetClientBookings.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    sealed class GetClientBookings{
        object Idle : GetClientBookings()
        object Loading : GetClientBookings()
        data class Success(val data: List<GetClientsBooking>) : GetClientBookings()
        data class Error(val message: String) : GetClientBookings()
    }*/
}