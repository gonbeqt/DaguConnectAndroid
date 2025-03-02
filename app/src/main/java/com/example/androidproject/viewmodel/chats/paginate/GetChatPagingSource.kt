package com.example.androidproject.viewmodel.chats.paginate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Chats

class GetChatPagingSource (
    private val apiService: ApiService
) : PagingSource<Int, Chats>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chats> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getChat(page = page, limit = params.loadSize)
            if (response.isSuccessful) {
                val chats = response.body()?.chats ?: emptyList()
                val nextPage = if (chats.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = chats,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextPage
                )
            } else {
                LoadResult.Error(Exception("Failed to load chats"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Chats>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?:state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}