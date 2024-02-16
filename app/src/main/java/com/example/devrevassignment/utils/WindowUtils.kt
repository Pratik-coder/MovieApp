package com.example.devrevassignment.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.example.devrevassignment.R

object WindowUtils {

    fun setUpLightTheme(activity: Activity){
        if(Build.VERSION.SDK_INT>=23){
            setLightStatusBar(activity)
        }
        if (Build.VERSION.SDK_INT>=26){
            setLightNavigationBar(activity)
        }
    }

    fun setUpDarkTheme(activity: Activity){
        if (Build.VERSION.SDK_INT>=23){
            setDarkStatusBar(activity)
        }
        if (Build.VERSION.SDK_INT>=26){
            setDarkNavigationBar(activity)
        }
    }

    fun setUpTransparentStatusBar(activity: Activity){
        if (Build.VERSION.SDK_INT>=23){
            activity.window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }



    fun setToolBarTopPadding(toolbar: androidx.appcompat.widget.Toolbar?){
        toolbar?.setOnApplyWindowInsetsListener{v,insets->
            v.updatePadding(top=insets.systemWindowInsetTop)
            insets
        }
    }

    fun clearStatusBarActivity(activity: Activity){
        if(Build.VERSION.SDK_INT>=23){
            setDarkStatusBar(activity,false)
        }
        var flags=activity.window.decorView.systemUiVisibility
        flags=flags and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        activity.window.decorView.systemUiVisibility=flags

    }

    @SuppressLint("SuspiciousIndentation")
    @TargetApi(23)
    private fun setLightStatusBar(activity: Activity, coloured:Boolean=true){
        var flags=activity.window.decorView.systemUiVisibility
        flags=flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility=flags

        if (coloured){
            activity.window.statusBarColor= ContextCompat.getColor(activity, R.color.colorStatusBarLight)
        }
    }

    @TargetApi(23)
    private fun setDarkStatusBar(activity: Activity, coloured:Boolean=true){
        var flags=activity.window.decorView.systemUiVisibility
        flags=flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        activity.window.decorView.systemUiVisibility=flags

        if (coloured){
            activity.window.statusBarColor= ContextCompat.getColor(activity,R.color.black)
        }
    }

    @TargetApi(26)
    private fun setLightNavigationBar(activity: Activity){
        var flags=activity.window.decorView.systemUiVisibility
        flags=flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        activity.window.decorView.systemUiVisibility=flags
        activity.window.navigationBarColor= Color.WHITE
    }

    @TargetApi(26)
    private fun setDarkNavigationBar(activity: Activity){
        var flags=activity.window.decorView.systemUiVisibility
        flags=flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        activity.window.decorView.systemUiVisibility=flags
        activity.window.navigationBarColor= ContextCompat.getColor(activity,R.color.black)
    }
}