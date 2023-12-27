package com.birdushenin.newssphere.navigation;

import com.birdushenin.newssphere.presentation.saved.SavedFragment;
import com.birdushenin.newssphere.presentation.saved.SavedWindowFragment;
import com.birdushenin.newssphere.presentation.sources.SourceFragment;
import com.github.terrakok.cicerone.Screen;

public class SavedNavigation {
    public static Screen SavedWindowFragment() {
        return new SavedWindowFragment();
    }

    public static Screen SavedFragment() {
        return new SavedFragment();
    }

    public static Screen SourceFragment() {
        return new SourceFragment();
    }
}
