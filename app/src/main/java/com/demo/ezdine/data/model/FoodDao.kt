package com.demo.ezdine.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table")
    fun getFoodList(): List<Food>

    @Query("SELECT * FROM food_table WHERE name LIKE:searchQuery" )
    fun getSearchedFoodList(searchQuery : String): List<Food>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: Food)

    @Query("SELECT EXISTS (SELECT * FROM food_table)")
    fun isFoodDataAvailable() : Boolean

}