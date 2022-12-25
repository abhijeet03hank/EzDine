package com.demo.ezdine.common

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class ApplicationPersistence {

    companion object {
        private val sharePref = ApplicationPersistence()
        private lateinit var sharedPreferences: SharedPreferences
        fun getInstance(context: Context): ApplicationPersistence {
            if (!::sharedPreferences.isInitialized) {
                synchronized(ApplicationPersistence::class.java) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
                    }
                }
            }
            return sharePref
        }
    }

    fun setStringValue(key: String, token: String) {
        sharedPreferences.edit()
            .putString(key, token)
            .apply()
    }

    fun getStringValue(key: String) : String? {
        return sharedPreferences.getString(key, "")
    }

    fun isContainsValue(key: String) : Boolean {
        return sharedPreferences.contains(key)
    }

    fun removeValue(key : String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

}