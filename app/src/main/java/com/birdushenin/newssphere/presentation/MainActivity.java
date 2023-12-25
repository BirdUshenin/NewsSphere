package com.birdushenin.newssphere.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.birdushenin.newssphere.MyApplication;
import com.birdushenin.newssphere.R;
import com.birdushenin.newssphere.navigation.BottomNavigation;
import com.birdushenin.newssphere.presentation.headlines.MainFragment;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Router;
import com.github.terrakok.cicerone.androidx.AppNavigator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    Router router;
    @Inject
    NavigatorHolder navigatorHolder;
    private final Fragment main = new MainFragment();
    private final AppNavigator navigator = new AppNavigator(this, R.id.fragment_container);

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.appComponent.inject(this);

        // TODO После реализации сплешскрина убрать
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, main, "1").commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Headlines:
                        navigateToTestScreen();
                        return true;
                    case R.id.Saved:
                        navigateToTestScreen2();
                        return true;
                    case R.id.Sources:
                        navigateToTestScreen3();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void navigateToTestScreen() {
        router.navigateTo(BottomNavigation.MainFragment());
    }
    private void navigateToTestScreen2() {
        router.navigateTo(BottomNavigation.SavedFragment());
    }
    private void navigateToTestScreen3() {
        router.navigateTo(BottomNavigation.SourceFragment());
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
        ((MyApplication) getApplication()).getNavigatorHolder().setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.setNavigator(navigator);
        ((MyApplication) getApplication()).getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    private void navigateToNewsWindowScreen() {
        ((MyApplication) getApplication()).getRouter().navigateTo(BottomNavigation.MainFragment());
    }

    private void navigateToFiltersFragment() {
        ((MyApplication) getApplication()).getRouter().navigateTo(BottomNavigation.SavedFragment());
    }

    private void navigateToSourceWindowFragment() {
        ((MyApplication) getApplication()).getRouter().navigateTo(BottomNavigation.SourceFragment());
    }
}