package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.navigation.Screens
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView

class TestFragment : Fragment() {

//    private val navigator = AppNavigator(requireActivity(), R.id.fragment_container2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)

        val bottomNavigationView: BottomNavigationView? = view.findViewById(R.id.bottom_navigation_fragment)

        bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Headlines -> {
                    navigateToNewsWindowScreen()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Saved -> {
                    navigateToFiltersFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Sources -> {
                    navigateToSourceWindowFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        return view
    }

//    override fun onResume() {
//        super.onResume()
//
//        // Проверяем, привязан ли фрагмент к активности перед выполнением операций
//        if (isAdded) {
//            (requireActivity().application as MyApplication).navigatorHolder.setNavigator(navigator)
//        }
//    }
//
//    override fun onPause() {
//        // Проверяем, привязан ли фрагмент к активности перед выполнением операций
//        if (isAdded) {
//            (requireActivity().application as MyApplication).navigatorHolder.removeNavigator()
//        }
//
//        super.onPause()
//    }

    private fun navigateToNewsWindowScreen() {
        (requireActivity().application as MyApplication).router.navigateTo(Screens.NewsWindowScreen)
    }

    private fun navigateToFiltersFragment() {
        (requireActivity().application as MyApplication).router.navigateTo(Screens.FiltersFragment)
    }

    private fun navigateToSourceWindowFragment() {
        (requireActivity().application as MyApplication).router.navigateTo(Screens.SourceWindowFragment)
    }

}