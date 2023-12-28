package com.birdushenin.newssphere.presentation.headlines.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.domain.usecases.BusinessUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class BusinessViewModel @Inject constructor(
    private val businessUseCase: BusinessUseCase
) : ViewModel() {
    val news: LiveData<List<Article>>
        get() = _news
    private val _news = MutableLiveData<List<Article>>()

    init {
        viewModelScope.launch {
            _news.value = loadNews("", null, null, null)
        }
    }

    fun updateNews(
        selectedFilter: String,
        selectedCalendarStart: String?,
        selectedCalendarEnd: String?,
        selectedLang: String?
    ) {
        viewModelScope.launch {
            _news.value = loadNews(
                selectedFilter,
                selectedCalendarStart,
                selectedCalendarEnd,
                selectedLang
            )
        }
    }

    private suspend fun loadNews(
        filter: String,
        fromDate: String?,
        toDate: String?,
        language: String?
    ): List<Article> {
        return businessUseCase.loadNews(filter, fromDate, toDate, language)
    }
}

class BusinessViewModelFactory @Inject constructor(
    myViewModelProvider: Provider<BusinessViewModel>
) : ViewModelProvider.Factory {

    private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
        BusinessViewModel::class.java to myViewModelProvider
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return providers[modelClass]!!.get() as T
    }
}
