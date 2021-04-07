package habibur.rahman.spark.tuition.ui.previous_class_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import habibur.rahman.spark.tuition.local_database.UserInfoDao

class PreviousClassListViewModelFactory(private val userInfoDao: UserInfoDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviousClassListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreviousClassListViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}