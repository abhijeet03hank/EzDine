package com.demo.ezdine.ui.sale

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.TransactionRepository
import com.demo.ezdine.data.repository.UserRepository
import com.demo.ezdine.viewmodel.LoginViewModel




class SaleViewModelFactory(private val application: Application, private val foodRepository: FoodRepository,private val transactionRepository: TransactionRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaleViewModel(application,foodRepository,transactionRepository) as T
    }
}