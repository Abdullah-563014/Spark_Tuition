package habibur.rahman.spark.tuition.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao

class PlayerActivityViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerActivityViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}