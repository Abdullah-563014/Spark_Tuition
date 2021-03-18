package habibur.rahman.spark.tuition.ui

import android.app.Application
import habibur.rahman.spark.tuition.local_database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    val appDatabase by lazy { AppDatabase.getDatabase(this) }


    override fun onCreate() {
        super.onCreate()
    }




}