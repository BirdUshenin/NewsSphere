package com.birdushenin.newssphere.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {

    @Provides
    @Singleton
    fun getCicerone(): Cicerone<Router> = create()

    @Provides
    @Singleton
    fun getRouter(cicerone: Cicerone<Router>) = cicerone.router

    @Provides
    @Singleton
    fun getNavigationHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()
}