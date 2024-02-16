package com.example.devrevassignment.extensions

import android.app.Activity
import android.content.Intent
import com.example.devrevassignment.ui.HomeActivity

fun Activity.navigateToHome(){
    this.startActivity(Intent(this, HomeActivity::class.java))
    this.finish()
}