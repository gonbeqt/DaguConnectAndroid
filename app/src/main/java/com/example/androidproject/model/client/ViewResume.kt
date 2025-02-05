import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class viewResume(
    @SerializedName("academic_background")
    val academicbackground: Any,
    @SerializedName("created_at")
    val createdat: String,
    val email: String,
    val id: Int,
    @SerializedName("prefered_work_location")
    val preferedworklocation: String,
    @SerializedName("profile_pic")
    val profilepic: String,
    val specialties: String,
    @SerializedName("tradesman_full_name")
    val tradesmanfullname: String,
    @SerializedName("updated_at")
    val updatedat: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workfee: Int,
    @SerializedName("about_me")
    val aboutme: String
)
{
    fun getAcademicBackground(): AcademicBackground? {
        return if (academicbackground is String) {
            Gson().fromJson(academicbackground, AcademicBackground::class.java)
        } else {
            academicbackground as? AcademicBackground
        }
    }
}


data class AcademicBackground(
    val Decription: String,
    @SerializedName("Field_of_study")
    val Fieldofstudy: String,
    val School: String
)
