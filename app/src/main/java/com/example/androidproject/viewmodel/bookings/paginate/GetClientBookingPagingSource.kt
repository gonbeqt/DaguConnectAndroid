package com.example.androidproject.viewmodel.bookings.paginate

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.resumesItem

class GetClientBookingPagingSource(private val apiService: ApiService) : PagingSource<Int, GetClientsBooking>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetClientsBooking> {
        return try {
            val page = params.key ?: 1
            Log.d("PagingSource", "Loading page $page")
            val response = apiService.getClientBooking(page, params.loadSize)
            val resumes = response.body()?.bookings ?: emptyList()
            Log.d("PagingSource", "Loaded ${resumes.size} items")
            LoadResult.Page(
                data = resumes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (resumes.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("PagingSource", "Error loading data", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetClientsBooking>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}