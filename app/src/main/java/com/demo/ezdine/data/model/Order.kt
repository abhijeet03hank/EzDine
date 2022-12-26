package com.demo.ezdine.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Order(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("food")
    val food: Food,
    @SerializedName("price")
    val price: String,
    @SerializedName("quantity")
    val quantity: Int
)