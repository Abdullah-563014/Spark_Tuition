package habibur.rahman.spark.tuition.network_database

import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {

    @GET("get_user_info.php?")
    fun getUserInfo(@Query("Email") email: String): Call<JsonElement>

    @POST("user_registration.php?")
    fun userRegistration(@Query("Name") name: String, @Query("Phone") phone: String, @Query("Email") email: String, @Query("Password") password: String, @Query("InstituteName") instituteName: String, @Query("ClassName") className: String, @Query("ActiveStatus") activeStatus: String): Call<JsonElement>

    @GET("get_tutors_list.php?")
    fun getTutorList(): Call<JsonElement>


    companion object{
        operator fun invoke() : MyApi{
            return Retrofit.Builder()
                .baseUrl(BuildConfig.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}