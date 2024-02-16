package com.example.devrevassignment.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devrevassignment.base.BaseActivity
import com.example.devrevassignment.extensions.navigateToHome

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToHome()
    }
}