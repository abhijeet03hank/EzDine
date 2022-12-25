package com.demo.ezdine.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "user_table",indices = [Index(value = ["name"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("password")
    var password : String?
) : Serializable