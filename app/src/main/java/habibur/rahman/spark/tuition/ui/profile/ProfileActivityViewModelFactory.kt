package habibur.rahman.spark.tuition.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao

class ProfileActivityViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileActivityViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}