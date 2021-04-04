package habibur.rahman.spark.tuition.ui.class_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import habibur.rahman.spark.tuition.local_database.UserInfoDao
import habibur.rahman.spark.tuition.model.UserInfoModel
import kotlinx.coroutines.Dispatchers

class ClassListViewModel(private val userInfoDao: UserInfoDao): ViewModel() {


    val userInfoLiveData: LiveData<UserInfoModel> = liveData(viewModelScope.coroutineContext+Dispatchers.IO){
        val data=userInfoDao.getUserInfo()
        emit(data)
    }


}