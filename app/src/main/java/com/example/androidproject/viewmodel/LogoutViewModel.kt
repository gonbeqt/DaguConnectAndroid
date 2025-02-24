import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LogoutViewModel(private val apiService: ApiService) : ViewModel() {
    private val _logoutResult = MutableStateFlow<Boolean?>(null)
    val logoutResult: StateFlow<Boolean?> = _logoutResult.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            try {
                val response = apiService.logout()
                _logoutResult.value = response.isSuccessful
            } catch (e: Exception) {
                _logoutResult.value = false
            }
        }
    }

    fun resetLogoutResult() {
        _logoutResult.value = null
    }
}