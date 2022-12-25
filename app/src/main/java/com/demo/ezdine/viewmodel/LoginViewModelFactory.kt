package com.demo.ezdine.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.UserRepository

class LoginViewModelFactory(private val application: Application,private val userRepository: UserRepository,private val foodRepository: FoodRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(application,userRepository,foodRepository) as T
    }
}