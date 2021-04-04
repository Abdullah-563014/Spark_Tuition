package habibur.rahman.spark.tuition.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.model.UserInfoModel
import habibur.rahman.spark.tuition.network_database.MyApi
import kotlinx.coroutines.launch


class LoginActivityViewModel(private var userInfoDao: UserInfoDao): ViewModel() {


    val getUserInfoFromStorage: LiveData<UserInfoModel> = liveData{
        val data=userInfoDao.getUserInfo()
        emit(data)
    }


    fun getUserInfoFromNetwork(email: String) = MyApi.invoke().getUserInfo(email)

    fun insert(userInfoModel: UserInfoModel) = viewModelScope.launch {
        userInfoDao.insert(userInfoModel)
    }


}