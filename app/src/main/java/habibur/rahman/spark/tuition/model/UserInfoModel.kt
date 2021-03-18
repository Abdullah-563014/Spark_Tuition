package habibur.rahman.spark.tuition.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "userInfoTable")
data class UserInfoModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "Name")
    @SerializedName("Name") val name : String,

    @ColumnInfo(name = "Phone")
    @SerializedName("Phone") val phone : String,

    @ColumnInfo(name = "Email")
    @SerializedName("Email") val email : String,

    @ColumnInfo(name = "Password")
    @SerializedName("Password") val password : String,

    @ColumnInfo(name = "InstituteName")
    @SerializedName("InstituteName") val instituteName : String,

    @ColumnInfo(name = "ClassName")
    @SerializedName("ClassName") val className : String,

    @ColumnInfo(name = "ActiveStatus")
    @SerializedName("ActiveStatus") val activeStatus : String


)
