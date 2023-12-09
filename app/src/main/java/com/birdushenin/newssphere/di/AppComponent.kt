package com.birdushenin.newssphere.di

import com.birdushenin.newssphere.presentation.HeadlinesFragment
import com.birdushenin.newssphere.presentation.NewsWindowFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component (modules = [RetrofitModule::class])

interface AppComponent {
    fun inject(fragment: HeadlinesFragment)
    fun inject(fragment: NewsWindowFragment)
}