package com.birdushenin.newssphere.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateViewModel: ViewModel() {
    private val _buttonClicked = MutableLiveData<Unit>()

    val buttonClicked: LiveData<Unit>
        get() = _buttonClicked

    fun onButtonClicked() {
        _buttonClicked.value = Unit
    }
}