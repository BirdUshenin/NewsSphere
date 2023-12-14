package com.birdushenin.newssphere.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.birdushenin.newssphere.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private var isSearchMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val appBarLayout: AppBarLayout = view.findViewById(R.id.appBarLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val btnSearch: ImageButton = view.findViewById(R.id.btnSearch)


        val pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position){
                0 -> "General"
                1 -> "Business"
                2 -> "Sports"
                else -> null
            }
            when(position){
                0 -> R.drawable.baseline_public
                1 -> R.drawable.query_stats
                2 -> R.drawable.baseline_sports
                else -> null
            }?.let { tab.setIcon(it) }
        }.attach()

        btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showSearchFragment()
            } else {
                hideSearchFragment()
            }
        }

        return view
    }

    private fun showSearchFragment() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        val searchFragment = SearchFragment()

        fragmentTransaction.replace(R.id.toolbar, searchFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        isSearchMode = true
    }

    private fun hideSearchFragment() {
        parentFragmentManager.popBackStack()
        isSearchMode = false
    }

    private class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> GeneralFragment()
                1 -> BusinessFragment()
                2 -> SportsFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}