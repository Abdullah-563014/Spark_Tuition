package habibur.rahman.spark.tuition.local_database

import androidx.lifecycle.LiveData
import androidx.room.*
import habibur.rahman.spark.tuition.model.UserInfoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Query("SELECT * FROM userInfoTable WHERE id=1 ORDER BY id ASC")
    fun getUserInfo(): UserInfoModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfoModel: UserInfoModel)

    @Delete
    suspend fun delete(userInfoModel: UserInfoModel)

    @Query("DELETE FROM userInfoTable")
    suspend fun deleteAll()


}