package com.example.androidproject.viewmodel.jobs.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetJobs

class GetRecentJobsPagingSource (
    private val apiService: ApiService
) : PagingSource<Int, GetJobs>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetJobs> {
        val page = params.key ?: 1 // Start from page 1 if key is null

        return try {
            val response = apiService.getRecentJobs(page = page, limit = params.loadSize)
            if (response.isSuccessful) {
                val jobs = response.body()?.jobs ?: emptyList()
                LoadResult.Page(
                    data = jobs,
                    prevKey = if (page == 1) null else page - 1, // No previous key if first page
                    nextKey = if (jobs.isEmpty()) null else page + 1 // No next page if data is empty
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetJobs>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}