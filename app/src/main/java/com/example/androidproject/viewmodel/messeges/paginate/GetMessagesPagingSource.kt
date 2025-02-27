import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidproject.api.ApiService
import com.example.androidproject.model.GetMessages

class GetMessagesPagingSource(
    private val apiService: ApiService,
    private val chatId: Int
) : PagingSource<Int, GetMessages>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetMessages> {
        val page = params.key ?: 1

        return try {
            val response = apiService.getConversation(chatId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val messages = responseBody?: emptyList()
                LoadResult.Page(
                    data = messages,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (messages.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetMessages>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
