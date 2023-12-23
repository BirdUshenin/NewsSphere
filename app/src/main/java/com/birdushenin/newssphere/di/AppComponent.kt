package com.birdushenin.newssphere.di

import com.birdushenin.newssphere.di.module.NavigationModule
import com.birdushenin.newssphere.di.module.RetrofitModule
import com.birdushenin.newssphere.presentation.GeneralFragment
import com.birdushenin.newssphere.presentation.BusinessFragment
import com.birdushenin.newssphere.presentation.MainActivity
import com.birdushenin.newssphere.presentation.SourceFragment
import com.birdushenin.newssphere.presentation.SourceWindowFragment
import com.birdushenin.newssphere.presentation.SportsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [RetrofitModule::class, NavigationModule::class])

interface AppComponent {
    fun inject(fragment: GeneralFragment)
    fun inject(fragment: BusinessFragment)
    fun inject(fragment: SportsFragment)
    fun inject(fragment: SourceFragment)
    fun inject(fragment: SourceWindowFragment)
    fun inject(activity: MainActivity)
}