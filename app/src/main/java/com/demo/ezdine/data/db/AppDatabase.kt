package com.demo.ezdine.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.model.FoodDao
import com.demo.ezdine.data.model.User
import com.demo.ezdine.data.model.UserDao

@Database(
    entities = [User::class , Food::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun foodDao(): FoodDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ez_dine_database"
                )
                    .fallbackToDestructiveMigration()
                    .build() // <---- The crash occurs here
                INSTANCE = instance
                return instance
            }
        }
    }

}