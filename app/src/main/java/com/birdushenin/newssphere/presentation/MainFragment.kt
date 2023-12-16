package com.birdushenin.newssphere.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.navigation.Screens
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private var isSearchMode = false
    private val searchViewModel: SearchViewModel by activityViewModels()
    val filterViewModel: FilterViewModel by activityViewModels()

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val appBarLayout: AppBarLayout = view.findViewById(R.id.appBarLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val btnSearch: ImageButton = view.findViewById(R.id.btnSearch)
        val btnSearchThis: ImageButton = view.findViewById(R.id.btnSearchThis)
        val editText: EditText = view.findViewById(R.id.editSearch)
        val btnFilter: ImageButton = view.findViewById(R.id.btnFilter)
        val toolbarTitle: TextView = view.findViewById(R.id.toolbar_title)

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

        btnFilter.setOnClickListener{
            (requireActivity().application as MyApplication).router.navigateTo(Screens.FiltersFragment)
        }

        btnSearchThis.setOnClickListener {
            val query = editText.text.toString()
            searchViewModel.searchQuery.value = query
        }

        btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showSearchFragment()
                btnFilter.visibility = View.GONE
                btnSearchThis.visibility = View.VISIBLE
                editText.visibility = View.VISIBLE
                toolbarTitle.visibility = View.GONE

            } else {
                hideSearchFragment()
                btnSearchThis.visibility = View.GONE
                editText.visibility = View.GONE
                btnFilter.visibility = View.VISIBLE
                toolbarTitle.visibility = View.VISIBLE
            }
        }

        return view
    }

    private fun showSearchFragment() {
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