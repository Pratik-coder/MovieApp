package com.example.devrevassignment.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.devrevassignment.R
import com.example.devrevassignment.base.BaseActivity
import com.example.devrevassignment.databinding.ActivityHomeBinding
import com.example.devrevassignment.extensions.setUpWithNavController

class HomeActivity : BaseActivity() {

    private lateinit var homeinding: ActivityHomeBinding
    private var currentNavController: LiveData<NavController>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeinding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeinding.root)
        setSupportActionBar(homeinding.toolbar)
        if (savedInstanceState==null){
            setUpBottomNavigationView()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigationView()
    }

    private fun setUpBottomNavigationView(){
        val controller=homeinding.bottomNavigation.setUpWithNavController(
            listOf(R.navigation.navigation_popular,R.navigation.navigation_upcoming),
            supportFragmentManager,
            R.id.nav_host_container,
            intent
        )
        controller.observe(this, Observer { navController->
            setupActionBarWithNavController(navController)
        })
        currentNavController=controller

    }

    override fun onSupportNavigateUp()=currentNavController?.value?.navigateUp() ?:false
}
