package com.example.androidproject.viewmodel.job_application.tradesman.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.JobApplicationData

class GetMyJobApplicationPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, JobApplicationData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JobApplicationData> {


        return try {
            val page = params.key ?: 1
            val response = apiService.getMyJobApplications(page = page, limit = params.loadSize)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val applicants = responseBody?.applications ?: emptyList() // Extract applications

                LoadResult.Page(
                    data = applicants,
                    prevKey = if (page == 1) null else page - 1, // No previous key if first page
                    nextKey = if (responseBody?.currentPage == responseBody?.totalPage) null else page + 1 // Stop if last page
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, JobApplicationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

