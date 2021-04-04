package habibur.rahman.spark.tuition.ui.class_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao

class ClassListViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClassListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClassListViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}