package com.demo.ezdine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "transaction_table",indices = [Index(value = ["id"], unique = true)])
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0 ,
    @SerializedName("first_name")
    var first_name: String,
    @SerializedName("last_name")
    var last_name: String,
    @SerializedName("total_price")
    var total_price: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("table_no")
    var table_no: String,
    @SerializedName("time_in_milis")
    var time_in_milis : Long?
) : Serializable