package habibur.rahman.spark.tuition.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao

class RegistrationActivityViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegistrationActivityViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}