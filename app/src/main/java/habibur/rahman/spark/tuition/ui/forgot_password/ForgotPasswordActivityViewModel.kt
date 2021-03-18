package habibur.rahman.spark.tuition.ui.forgot_password

import androidx.lifecycle.ViewModel
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.network_database.MyApi

class ForgotPasswordActivityViewModel(private var userInfoDao: UserInfoDao): ViewModel() {


    fun getUserInfoFromNetwork(email: String) = MyApi.invoke().getUserInfo(email)


}