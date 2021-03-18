package habibur.rahman.spark.tuition.ui.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.ui.login.LoginActivityViewModel

class ForgotPasswordActivityViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForgotPasswordActivityViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}