package com.birdushenin.newssphere.navigation;

import com.birdushenin.newssphere.presentation.saved.SavedFragment;
import com.birdushenin.newssphere.presentation.saved.SavedWindowFragment;
import com.github.terrakok.cicerone.Screen;

public class SavedNavigation {
    public static Screen SavedWindowFragment() {
        return new SavedWindowFragment();
    }
    public static Screen SavedFragment() {
        return new SavedFragment();
    }
}
