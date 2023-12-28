package com.birdushenin.newssphere.presentation.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.domain.usecases.SourceUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SourceGlobalViewModel @Inject constructor(
    private val sourceUseCase: SourceUseCase
) : ViewModel() {

    private val _news = MutableLiveData<List<SourceNews>>()
    val news: LiveData<List<SourceNews>> get() = _news

    fun loadNews() {
        viewModelScope.launch {
            _news.value = sourceUseCase.loadNews()
        }
    }
}

class SourceViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<SourceGlobalViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        SourceGlobalViewModel::class.java to myViewModelProvider
    )
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
