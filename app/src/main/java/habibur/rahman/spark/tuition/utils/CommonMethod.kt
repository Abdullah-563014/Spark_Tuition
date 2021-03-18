package habibur.rahman.spark.tuition.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.model.UserInfoModel

object CommonMethod {

    fun openUrl(context: Context, url: String) {
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW,Uri.parse(url)),context.resources.getString(R.string.choose_one)))
    }

    suspend fun loadPreviousUserInfoFromStorage(context: Context): String?  {
        val gson: Gson = Gson()
        var dataString: String?=null
        dataString=SharedPreUtils.getStringFromStorage(
            context.applicationContext,
            Constants.userInfoModelKey,
            null
        )
        return dataString
    }

    fun saveUserInfoToStorage(context: Context, model: UserInfoModel) {
        val gson: Gson= Gson()
        val stringData: String=gson.toJson(model)
        Coroutines.io {
            SharedPreUtils.setStringToStorage(context.applicationContext, Constants.userInfoModelKey, stringData)
        }
    }




}