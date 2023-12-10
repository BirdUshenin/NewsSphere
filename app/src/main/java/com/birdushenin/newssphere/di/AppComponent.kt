package com.birdushenin.newssphere.di

import com.birdushenin.newssphere.presentation.GeneralFragment
import com.birdushenin.newssphere.presentation.BusinessFragment
import com.birdushenin.newssphere.presentation.SportsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [RetrofitModule::class])

interface AppComponent {
    fun inject(fragment: GeneralFragment)
    fun inject(fragment: BusinessFragment)
    fun inject(fragment: SportsFragment)
}