package com.birdushenin.newssphere.navigation;

import com.birdushenin.newssphere.presentation.headlines.MainFragment;
import com.birdushenin.newssphere.presentation.saved.SavedFragment;
import com.birdushenin.newssphere.presentation.sources.SourceFragment;
import com.github.terrakok.cicerone.Screen;

public class BottomNavigation {
    public static Screen MainFragment() {
        return new MainFragment();
    }

    public static Screen SavedFragment() {
        return new SavedFragment();
    }

    public static Screen SourceFragment() {
        return new SourceFragment();
    }

}
