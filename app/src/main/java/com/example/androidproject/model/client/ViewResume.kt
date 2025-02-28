import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class viewResume(
    @SerializedName("created_at")
    val createdat: String,
    val email: String,
    val id: Int,
    @SerializedName("prefered_work_location")
    val preferedworklocation: String,
    @SerializedName("profile_pic")
    val profilepic: String,
    @SerializedName("phone_number")
    val phonenumber : String,
    val specialty: String?,
    @SerializedName("tradesman_full_name")
    val tradesmanfullname: String,
    @SerializedName("updated_at")
    val updatedat: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workfee: Int,
    val ratings: Float,
    val documents : String,
    @SerializedName("about_me")
    val aboutme: String,
    @SerializedName("is_approve")
    val isapprove: Int,
    @SerializedName("status_of_approval")
    val statusofapproval: String

)
