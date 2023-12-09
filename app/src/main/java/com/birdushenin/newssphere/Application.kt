package com.birdushenin.newssphere

import android.app.Application
import com.birdushenin.newssphere.di.AppComponent
import com.birdushenin.newssphere.di.DaggerAppComponent

class Application: Application() {
    companion object{
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}