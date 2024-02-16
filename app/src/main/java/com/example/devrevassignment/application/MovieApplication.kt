package com.example.devrevassignment.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.example.devrevassignment.modules.mainModule
import com.example.devrevassignment.modules.movieDetailModule
import com.example.devrevassignment.modules.popularModule
import com.example.devrevassignment.modules.upcomingModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@MovieApplication)
            modules(mainModule, popularModule, upcomingModule, movieDetailModule)
        }
        setUpTimberLog()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun setUpTimberLog(){
        Timber.plant(object : Timber.DebugTree(){
            override fun createStackElementTag(element: StackTraceElement): String? {
                return String.format(
                    "%s::%s:%s",
                    super.createStackElementTag(element),
                    element.methodName,
                    element.lineNumber
                )
            }
        })
    }
}