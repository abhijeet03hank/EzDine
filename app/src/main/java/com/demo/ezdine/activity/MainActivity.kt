package com.demo.ezdine.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.demo.ezdine.R
import com.demo.ezdine.common.ApplicationPersistence
import com.demo.ezdine.common.Constants
import com.demo.ezdine.data.model.User
import com.demo.ezdine.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val TAG ="MainActivity"

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        getSessionUser()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_sale, R.id.nav_order, R.id.logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(
            NavigationView.OnNavigationItemSelectedListener { item ->
                var id = item.itemId
                when(id){
                    R.id.nav_sale -> {
                        navController.navigate(R.id.nav_sale)
//                        navController.popBackStack(R.id.nav_sale,false)
                    }
                    R.id.nav_order -> {
                        navController.navigate(R.id.nav_order)
//                        navController.popBackStack(R.id.nav_order,false)
                    }
                    R.id.logout -> {
                        Toast.makeText(this@MainActivity, "logout", Toast.LENGTH_SHORT).show()
                        ApplicationPersistence.getInstance(this@MainActivity).clearAll()
                        var intent = Intent(this@MainActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                drawerLayout.closeDrawer(GravityCompat.START)
                return@OnNavigationItemSelectedListener true
            })
    }

    private fun getSessionUser(){
        try{
            if(ApplicationPersistence.getInstance(this@MainActivity).isContainsValue(Constants.SESSION_USER)){
                val sessionUserJson = ApplicationPersistence.getInstance(this@MainActivity).getStringValue(Constants.SESSION_USER)
                val gson = Gson()
                val userType = object : TypeToken<User>() {}.type
                user = gson.fromJson<User>(sessionUserJson, userType)
            }else {
                val bundle = intent.extras
                bundle?.let {
                    user = it.get(Constants.USER_DATA) as User
                }
            }
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = searchItem?.actionView as SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        val view = binding.navView.getHeaderView(0)
        user?.let {
            view.findViewById<TextView>(R.id.tvUserName).text = it.name
            view.findViewById<TextView>(R.id.tvUserType).text = it.type
        }
        return true
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchQuery(newText)
        }
        return true
    }

    private fun searchQuery(query: String?){
        val searchQuery = "%$query%"
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}