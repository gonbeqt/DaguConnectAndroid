package com.example.androidproject.viewmodel.notifications.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetMessages
import com.example.androidproject.model.Notification

class GetNotificationPagingSource (
    private val apiService: ApiService,
) : PagingSource<Int, Notification>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getNotifications(page = page, limit = params.loadSize)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val notification = responseBody?.notifications?: emptyList()
                LoadResult.Page(
                    data = notification,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (notification.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}