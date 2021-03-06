package habibur.rahman.spark.tuition.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Insets
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.widget.Toast
import com.google.gson.Gson
import com.scottyab.aescrypt.AESCrypt
import habibur.rahman.spark.tuition.BuildConfig
import habibur.rahman.spark.tuition.R
import habibur.rahman.spark.tuition.model.UserInfoModel


object CommonMethod {

    fun openUrl(context: Context, url: String) {
        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_VIEW, Uri.parse(url)), context.resources.getString(
                    R.string.choose_one
                )
            )
        )
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
            SharedPreUtils.setStringToStorage(
                context.applicationContext,
                Constants.userInfoModelKey,
                stringData
            )
        }
    }

    fun getScreenHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    fun openWhatsApp(context: Context, number: String) {
//        val contact = "+8801750179685"
        val url = "https://api.whatsapp.com/send?phone=$number"
        try {
            val pm: PackageManager = context.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.whatsapp_not_installed),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun openAppLink(context: Context) {
        val appPackageName: String=context.applicationContext.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun encrypt(message: String): String {
        return AESCrypt.encrypt(BuildConfig.SMTP_PASSWORD,message)
    }

    fun decrypt(message: String): String {
        return AESCrypt.decrypt(BuildConfig.SMTP_PASSWORD,message)
    }

    fun filterMessage(message: String?): String {
        if (message!=null) {
            if (message.contains("app-special-domain.sparktuitionservice.com")) {
                return message.replace("app-special-domain.sparktuitionservice.com","server")
            } else {
                return message
            }
        } else {
            return " "
        }
    }




}