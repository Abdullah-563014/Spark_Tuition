package habibur.rahman.spark.tuition.local_database

import androidx.room.*
import habibur.rahman.spark.tuition.model.UserInfoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {

    @Query("SELECT * FROM userInfoTable WHERE id=1 ORDER BY id ASC")
    fun getUserInfo(): Flow<UserInfoModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userInfoModel: UserInfoModel)

    @Delete
    suspend fun delete(userInfoModel: UserInfoModel)

    @Query("DELETE FROM userInfoTable")
    suspend fun deleteAll()


}