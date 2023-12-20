package com.birdushenin.newssphere.domain

import com.birdushenin.newssphere.data.SavedClass

interface OnSavedItemClickListener {
    fun onSavedItemClicked(savedClass: SavedClass)
}