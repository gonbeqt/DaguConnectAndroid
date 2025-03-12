import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.Conversation
import com.example.androidproject.model.GetMessages

class GetMessagesPagingSource(
    private val apiService: ApiService,
    private val chatId: Int
) : PagingSource<Int, Conversation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Conversation> {
        val page = params.key ?: 1
        Log.d("API_DEBUG", "Fetching messages for chatId: $chatId, Page: $page, LoadSize: ${params.loadSize}")

        return try {
            val response = apiService.getConversation(chatId, page, params.loadSize)
            if (response.isSuccessful) {
                val messages = response.body()?.messages ?: emptyList()
                Log.d("API_RESPONSE", "Response: ${response.body()}")
                Log.d("API_RESPONSE", "Messages: $messages")
                LoadResult.Page(
                    data = messages,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (messages.isEmpty()) null else page + 1
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

    override fun getRefreshKey(state: PagingState<Int, Conversation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
