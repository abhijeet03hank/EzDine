package com.demo.ezdine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "food_table",indices = [Index(value = ["name"], unique = true)])
data class Food(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("img_url")
    val img_url: String,
    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("type")
    val type: String
)