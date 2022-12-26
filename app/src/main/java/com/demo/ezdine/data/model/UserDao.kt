package com.demo.ezdine.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE password LIKE:pin")
      fun getUser(pin: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertUser(user: User)

    @Query("SELECT EXISTS (SELECT * FROM user_table)")
    fun isUsersDataAvailable() : Boolean

}