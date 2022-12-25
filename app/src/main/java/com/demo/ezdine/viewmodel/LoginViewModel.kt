package com.demo.ezdine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.ezdine.MyApplication
import com.demo.ezdine.data.model.User
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appContext: Application,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository

) : AndroidViewModel(appContext)  {

    private val _userDataExist = MutableLiveData<Boolean>(true)
    val userDataExist : LiveData<Boolean> = _userDataExist

    private val _foodDataExist = MutableLiveData<Boolean>(true)
    val foodDataExist : LiveData<Boolean> = _foodDataExist

    private var _user : MutableLiveData<List<User>?> = MutableLiveData()
    val user : LiveData<List<User>?> = _user


    fun getValidUser(pin : String){
        viewModelScope.launch(Dispatchers.IO) {
            var usersList = userRepository.getUser(pin)
            _user.postValue(usersList as List<User>?)
        }
    }

    fun isUsersDataAvailable(){
        viewModelScope.launch(Dispatchers.IO) {
            _userDataExist.postValue(userRepository.isUsersDataAvailable())
        }
    }

    fun insertUserData(){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUserData(appContext)
        }
    }

    fun insertFoodData(){
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.insertFoodData(appContext)
        }
    }

    fun isFoodDataAvailable(){
        viewModelScope.launch(Dispatchers.IO) {
            _foodDataExist.postValue(foodRepository.isFoodDataAvailable())
        }
    }


}