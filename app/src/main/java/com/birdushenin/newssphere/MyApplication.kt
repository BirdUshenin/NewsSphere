package com.birdushenin.newssphere

import android.app.Application
import com.birdushenin.newssphere.di.AppComponent
import com.birdushenin.newssphere.di.DaggerAppComponent
import com.github.terrakok.cicerone.Cicerone

class MyApplication: Application() {

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    companion object {
        lateinit var appComponent: AppComponent
        internal lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        INSTANCE = this
    }
}
