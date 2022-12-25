package com.demo.ezdine.data.repository

import android.content.Context
import android.util.Log
import com.demo.ezdine.R
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.Food
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

class FoodRepository(
    private val appDatabase: AppDatabase
) {
    private val TAG ="FoodRepository"

    private suspend fun insertFood(food: Food) = appDatabase.foodDao().insertFood(food)

    suspend fun getFoodList()  =  appDatabase.foodDao().getFoodList()

    suspend fun isFoodDataAvailable()  =  appDatabase.foodDao().isFoodDataAvailable()

    suspend fun insertFoodData(context: Context){
        try{
            var foodJsonArray = loadJSONArray(context)
            var foodList = ArrayList<Food>()
            val gson = Gson()
            val foodListType = object : TypeToken<ArrayList<Food?>?>() {}.type
            foodList = gson.fromJson<ArrayList<Food>>(foodJsonArray, foodListType)

            for (food in foodList) {
                insertFood(food)
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun loadJSONArray(context: Context):String?{
        try{
            val inputStream = context.resources.openRawResource(R.raw.food_menu)
            BufferedReader(inputStream.reader()).use {
                return it.readText()
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
            return null
        }
    }

}
