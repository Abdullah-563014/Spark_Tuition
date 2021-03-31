package habibur.rahman.spark.tuition.ui

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.onesignal.OneSignal
import habibur.rahman.spark.tuition.local_database.AppDatabase
import habibur.rahman.spark.tuition.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val appDatabase by lazy { AppDatabase.getDatabase(this) }
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate() {
        super.onCreate()

        initOneSignal()

        initFirebaseServices()


    }


    private fun initOneSignal() {
        OneSignal.initWithContext(this)
        OneSignal.setAppId(Constants.ONESIGNAL_APP_ID)
    }

    private fun initFirebaseServices() {
        firebaseAnalytics = Firebase.analytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }



}