package habibur.rahman.spark.tuition.network_database

import com.google.gson.JsonElement
import habibur.rahman.spark.tuition.BuildConfig
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import java.util.concurrent.TimeUnit

interface MyApi {


    @GET("get_user_info.php")
    fun getUserInfo(@Query("Email") email: String): Call<JsonElement>

    @POST("user_registration.php")
    fun userRegistration(@Query("Name") name: String, @Query("Phone") phone: String, @Query("Email") email: String, @Query("Password") password: String, @Query("InstituteName") instituteName: String, @Query("ClassName") className: String, @Query("ActiveStatus") activeStatus: String): Call<JsonElement>

    @GET("get_tutors_list.php")
    fun getTutorList(): Call<JsonElement>

    @GET("get_live_support_list.php")
    fun getLiveSupportList(): Call<JsonElement>

    @GET("get_previous_video_list.php")
    fun getPreviousVideoList(@Query("ClassName") className: String): Call<JsonElement>

    @GET("get_live_video_list.php")
    fun getLiveVideoList(@Query("ClassName") className: String): Call<JsonElement>



    companion object{
        operator fun invoke() : MyApi{

            val okHttpClient: OkHttpClient= OkHttpClient.Builder()
                .cache(null)
                .build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.MAIN_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}