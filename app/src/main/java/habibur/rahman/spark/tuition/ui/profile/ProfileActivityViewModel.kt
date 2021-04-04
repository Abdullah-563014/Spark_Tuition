package habibur.rahman.spark.tuition.ui.profile

import androidx.lifecycle.*
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.model.UserInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivityViewModel(private val userInfoDao: UserInfoDao):ViewModel() {

    val userInfoLiveData: LiveData<UserInfoModel> = liveData(viewModelScope.coroutineContext+Dispatchers.IO){
        val data=userInfoDao.getUserInfo()
        emit(data)
    }





}