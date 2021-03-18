package habibur.rahman.spark.tuition.ui.registration

import androidx.lifecycle.ViewModel
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.network_database.MyApi

class RegistrationActivityViewModel(private val userInfoDao: UserInfoDao): ViewModel() {


    fun createNewAccount(name: String, phone: String, email: String, password: String, instituteName: String, className: String, activeStatus: String) = MyApi.invoke().userRegistration(name,phone,email, password, instituteName, className, activeStatus)





}