import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Conversation

class GetMessagesPagingSource(
    private val apiService: ApiService,
    private val chatId: Int
) : PagingSource<Int, Conversation>() {

    private var lastPage: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Conversation> {
        val page = params.key ?: getLastPage()
        Log.d("API_DEBUG", "Fetching messages for chatId: $chatId, Page: $page, LoadSize: ${params.loadSize}")

        return try {
            val response = apiService.getConversation(chatId, page, params.loadSize)
            if (response.isSuccessful) {
                val messages = response.body()?.messages ?: emptyList()

                Log.d("API_RESPONSE", "Response: ${response.body()}")
                Log.d("API_RESPONSE", "Messages: $messages")

                LoadResult.Page(
                    data = messages,
                    prevKey = if (page > 1) page - 1 else null,  // Load older messages when scrolling up
                    nextKey = if (page < lastPage!!) page + 1 else null // Prevents fetching past the latest messages
                )
            } else {
                Log.d("API_RESPONSE", "Error: ${response.code()} ${response.message()}")
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("API_RESPONSE", "Exception: ${e.message}")
            LoadResult.Error(e)
        }
    }

    private suspend fun getLastPage(): Int {
        return lastPage ?: run {
            val response = apiService.getConversation(chatId, 1, 1) // Fetch total pages
            (if (response.isSuccessful) {
                val totalPages = response.body()?.totalPages
                lastPage = totalPages
                totalPages
            } else {
                1 // Default to first page if API fails
            }) as Int
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Conversation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
