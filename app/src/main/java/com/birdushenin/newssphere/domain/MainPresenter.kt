package com.birdushenin.newssphere.domain

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {
    private var isSearchMode: Boolean = false

    override fun onViewCreated() {
        if (isSearchMode) {
            view.showSearchFragment()
        } else {
        }
    }

    override fun onSearchButtonClick() {
        isSearchMode = !isSearchMode
        if (isSearchMode) {
            view.showSearchFragment()
        } else {
            view.hideSearchFragment()
        }
    }
}