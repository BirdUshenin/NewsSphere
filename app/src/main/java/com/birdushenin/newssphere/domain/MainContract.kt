package com.birdushenin.newssphere.domain

interface MainContract {
    interface View {
        fun showSearchFragment()
        fun hideSearchFragment()
        fun updateImageBasedOnFilterCount(count: Int)
    }

    interface Presenter {
        fun onViewCreated()
        fun onSearchButtonClick()
    }
}