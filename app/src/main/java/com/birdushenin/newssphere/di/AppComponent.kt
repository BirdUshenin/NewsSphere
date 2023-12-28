package com.birdushenin.newssphere.di

import android.content.Context
import com.birdushenin.newssphere.di.module.DatabaseModule
import com.birdushenin.newssphere.di.module.NavigationModule
import com.birdushenin.newssphere.di.module.RetrofitModule
import com.birdushenin.newssphere.domain.ClearOldDataWorker
import com.birdushenin.newssphere.presentation.MainActivity
import com.birdushenin.newssphere.presentation.headlines.business.BusinessFragment
import com.birdushenin.newssphere.presentation.headlines.business.BusinessViewModelFactory
import com.birdushenin.newssphere.presentation.headlines.general.GeneralFragment
import com.birdushenin.newssphere.presentation.headlines.general.GeneralViewModelFactory
import com.birdushenin.newssphere.presentation.headlines.sports.SportsFragment
import com.birdushenin.newssphere.presentation.headlines.sports.SportsViewModelFactory
import com.birdushenin.newssphere.presentation.saved.SavedFragment
import com.birdushenin.newssphere.presentation.sources.SourceFragment
import com.birdushenin.newssphere.presentation.sources.SourceViewModelFactory
import com.birdushenin.newssphere.presentation.sources.SourceWindowFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, NavigationModule::class, DatabaseModule::class])
interface AppComponent {
    fun inject(fragment: GeneralFragment)
    fun inject(fragment: BusinessFragment)
    fun inject(fragment: SportsFragment)
    fun inject(fragment: SourceFragment)
    fun inject(fragment: SourceWindowFragment)
    fun inject(fragment: SavedFragment)
    fun inject(activity: MainActivity)
    fun inject(worker: ClearOldDataWorker)
    fun viewModelsFactory(): GeneralViewModelFactory
    fun viewModelBusinessFactory(): BusinessViewModelFactory
    fun viewModelSportsFactory(): SportsViewModelFactory
    fun sourceViewModelFactory(): SourceViewModelFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}