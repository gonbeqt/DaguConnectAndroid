import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.model.JobLocation
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException



class MapViewModel : ViewModel() {
    private val client = OkHttpClient()

    var selectedLocation: JobLocation? = null
        private set

    var jobLocations: List<JobLocation> = emptyList()
        private set

    fun setLocation(lat: Double, lng: Double) {
        selectedLocation = JobLocation(lat, lng)
    }

    fun saveLocationToServer() {
        selectedLocation?.let { job ->
            val url = "https://yourapi.com/api/save-location"
            val json = JSONObject().apply {
                put("latitude", job.latitude)
                put("longitude", job.longitude)
            }

            val requestBody = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to save location: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    println("Location saved successfully")
                }
            })
        }
    }

    fun fetchJobLocations() {
        viewModelScope.launch {
            val url = "https://yourapi.com/api/get-locations"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to fetch locations: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.string()?.let { responseData ->
                        val jobsArray = JSONArray(JSONObject(responseData).getString("jobs"))
                        jobLocations = (0 until jobsArray.length()).map {
                            val job = jobsArray.getJSONObject(it)
                            JobLocation(job.getDouble("latitude"), job.getDouble("longitude"))
                        }
                    }
                }
            })
        }
    }
}
