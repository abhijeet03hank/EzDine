package com.demo.ezdine.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transaction_table ")
     fun getTransactionList(): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertTransaction(transaction: Transaction)
}