package com.demo.ezdine.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.demo.ezdine.common.ApplicationPersistence
import com.demo.ezdine.common.Constants
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.User
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.UserRepository
import com.demo.ezdine.databinding.ActivityLoginBinding
import com.demo.ezdine.viewmodel.LoginViewModel
import com.demo.ezdine.viewmodel.LoginViewModelFactory
import com.google.gson.Gson


class LoginActivity : AppCompatActivity() {
    private val TAG ="LoginActivity"

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var foodRepository: FoodRepository
    private lateinit var loginViewModelFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            checkForLoggedUser()

            val database = AppDatabase.getDatabase(applicationContext)
            userRepository = UserRepository(database)
            foodRepository = FoodRepository(database)

            loginViewModelFactory =
                LoginViewModelFactory(application, userRepository, foodRepository)
            loginViewModel = ViewModelProvider(
                this@LoginActivity,
                loginViewModelFactory
            )[LoginViewModel::class.java]

            initClickListeners()
            initObservers()
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun checkForLoggedUser(){
        try{
            if(ApplicationPersistence.getInstance(this@LoginActivity).isContainsValue(Constants.SESSION_USER)){
                var intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun initObservers(){
        try {
            if (::loginViewModel.isInitialized) {
                loginViewModel.isUsersDataAvailable()
                loginViewModel.isFoodDataAvailable()


                loginViewModel.userDataExist.observe(this, Observer { value ->
                    if (!value) {
                        loginViewModel.insertUserData()
                    }
                })

                loginViewModel.foodDataExist.observe(this, Observer { value ->
                    if (!value) {
                        loginViewModel.insertFoodData()
                    }
                })

                loginViewModel.user.observe(this, Observer { user ->
                    if (!user.isNullOrEmpty()) {
                        val currentUser = user.first()
                        saveUserSession(currentUser)
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    intent.putExtra(Constants.USER_DATA,user.first() as User)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.tvError.text = "Please enter correct Pin!"
                        binding.tvError.visibility = View.VISIBLE
                    }
                })
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun initClickListeners(){
        try{
            binding.logginButton.setOnClickListener {
                if(binding.tiedPin.text.isNullOrEmpty()){
                    binding.tvError.text = "Please enter the Pin"
                    binding.tvError.visibility = View.VISIBLE
                }else{
                    binding.tvError.visibility = View.GONE
                    loginViewModel.getValidUser(binding.tiedPin.text.toString())
                }
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }

    private fun saveUserSession(currentUser : User){
        try{
            ApplicationPersistence.getInstance(this@LoginActivity)?.let {
                it.clearAll()
                var sessionUser = User(currentUser.id,currentUser.name,currentUser.type,null)
                val gson = Gson()
                val sessionUserJson = gson.toJson(sessionUser)
                it.setStringValue(Constants.SESSION_USER,sessionUserJson)
            }
        }catch (e: Exception){
            Log.d(TAG,e.toString())
        }
    }
}