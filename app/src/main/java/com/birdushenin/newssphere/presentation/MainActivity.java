package com.birdushenin.newssphere.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.birdushenin.newssphere.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    private final Fragment headlines = new HeadlinesFragment();

    private final Fragment main = new MainFragment();
    private final Fragment saved = new SavedFragment();
    private final Fragment sources = new SourceFragment();
    private Fragment activeFragment = main;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, sources, "3").hide(sources).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, saved, "2").hide(saved).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, main, "1").commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Headlines:
                    switchFragment(main);
                    return true;
                case R.id.Saved:
                    switchFragment(saved);
                    return true;
                case R.id.Sources:
                    switchFragment(sources);
                    return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(targetFragment)
                .commit();
        activeFragment = targetFragment;
    }
}