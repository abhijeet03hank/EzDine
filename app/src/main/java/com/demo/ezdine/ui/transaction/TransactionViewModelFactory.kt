package com.demo.ezdine.ui.transaction

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.TransactionRepository
import com.demo.ezdine.ui.sale.SaleViewModel

class TransactionViewModelFactory(private val application: Application, private val transactionRepository: TransactionRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionViewModel(application,transactionRepository) as T
    }
}