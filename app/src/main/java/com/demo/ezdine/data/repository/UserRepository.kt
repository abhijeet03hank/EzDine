package com.demo.ezdine.data.repository

import android.content.Context
import android.util.Log
import com.demo.ezdine.R
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader

class UserRepository(
        private val appDatabase: AppDatabase
) {
    private val TAG ="UserRepository"

    private suspend fun insertUser(user: User) = appDatabase.userDao().insertUser(user)

    suspend fun getUser(pin : String)  =  appDatabase.userDao().getUser(pin)

    suspend fun isUsersDataAvailable()  =  appDatabase.userDao().isUsersDataAvailable()

    suspend fun insertUserData(context: Context){
        try {
            var userJsonArray = loadJSONArray(context)
            var userList = ArrayList<User>()
            val gson = Gson()
            val userListType = object : TypeToken<ArrayList<User?>?>() {}.type
            userList = gson.fromJson<ArrayList<User>>(userJsonArray, userListType)

            for (user in userList) {
                insertUser(user)
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun loadJSONArray(context: Context):String?{
        try{
            val inputStream = context.resources.openRawResource(R.raw.users)
            BufferedReader(inputStream.reader()).use {
                return it.readText()
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
            return null
        }
    }

}