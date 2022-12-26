package com.demo.ezdine.data.repository

import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.Transaction

class TransactionRepository(
    private val appDatabase: AppDatabase
) {
    private val TAG ="TransactionRepository"

     suspend fun insertTransaction(transaction: Transaction) = appDatabase.transactionDao().insertTransaction(transaction)

    suspend fun getTransactionList()  =  appDatabase.transactionDao().getTransactionList()
}