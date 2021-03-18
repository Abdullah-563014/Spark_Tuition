package habibur.rahman.spark.tuition.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPreUtils{

    var sharedPreferences: SharedPreferences?=null

    private fun initSharedPref(context: Context): SharedPreferences {
        if (sharedPreferences==null) {
            sharedPreferences=context.getSharedPreferences(Constants.sharedPreferenceName,MODE_PRIVATE)
        }
        return sharedPreferences!!
    }

    suspend fun setStringToStorage(context: Context,key: String, value: String) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putString(key,value)
        editor.apply()
    }

    suspend fun setBooleanToStorage(context: Context,key: String, value: Boolean) {
        val editor: SharedPreferences.Editor=initSharedPref(context).edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    suspend fun getStringFromStorage(context: Context,key: String, defaultValue: String?) : String? {
        return initSharedPref(context).getString(key,defaultValue)
    }

    suspend fun getBooleanFromStorage(context: Context,key: String, defaultValue: Boolean) : Boolean {
        return initSharedPref(context).getBoolean(key,defaultValue)
    }





}