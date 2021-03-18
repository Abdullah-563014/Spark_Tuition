package habibur.rahman.spark.tuition.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import habibur.rahman.spark.tuition.network_database.MyApi
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.model.UserInfoModel
import kotlinx.coroutines.launch

class LoginActivityViewModel(private var userInfoDao: UserInfoDao): ViewModel() {


    fun getUserInfoFromNetwork(email: String) = MyApi.invoke().getUserInfo(email)

    val getUserInfoFromStorage: LiveData<UserInfoModel> = userInfoDao.getUserInfo().asLiveData()

    fun insert(userInfoModel: UserInfoModel) = viewModelScope.launch {
        userInfoDao.insert(userInfoModel)
    }


}