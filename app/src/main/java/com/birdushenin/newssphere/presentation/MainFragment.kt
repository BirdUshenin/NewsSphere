package com.birdushenin.newssphere.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.domain.MainContract
import com.birdushenin.newssphere.domain.MainPresenter
import com.birdushenin.newssphere.navigation.Screens
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment(), FragmentScreen, MainContract.View{

    private lateinit var presenter: MainContract.Presenter

    private lateinit var updateViewModel: UpdateViewModel
    private val filterViewModel: FilterViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private var isSearchMode = false

    override fun createFragment(factory: FragmentFactory): Fragment {
        return MainFragment()
    }

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = MainPresenter(this)
        presenter.onViewCreated()


        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val btnSearch: ImageButton = view.findViewById(R.id.btnSearch)
        val btnSearchThis: ImageButton = view.findViewById(R.id.btnSearchThis)
        val editText: EditText = view.findViewById(R.id.editSearch)
        val btnFilter: ImageView = view.findViewById(R.id.btnFilter)
        val toolbarTitle: TextView = view.findViewById(R.id.toolbar_title)
        val btnSearchBack: ImageButton = view.findViewById(R.id.btnSearchBack)
        updateViewModel = ViewModelProvider(requireActivity())[UpdateViewModel::class.java]

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

        btnSearchBack.setOnClickListener {
            hideKeyboard()
            updateViewModel.onButtonClicked()
            hideSearchFragment()
            btnSearchThis.visibility = View.GONE
            editText.visibility = View.GONE
            btnFilter.visibility = View.VISIBLE
            toolbarTitle.visibility = View.VISIBLE
            btnSearch.visibility = View.VISIBLE
            btnSearchBack.visibility = View.GONE
        }

        btnFilter.setOnClickListener{
            (requireActivity().application as MyApplication).router.navigateTo(Screens.FiltersFragment)
        }

        btnSearchThis.setOnClickListener {
            editText.text.clear()
        }

        filterViewModel.filterCountEvent.observe(viewLifecycleOwner, Observer {
            updateImageBasedOnFilterCount(filterViewModel.appliedFilterCount.value ?: 0)
        })

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = editText.text.toString()
                searchViewModel.searchQuery.value = query
                return@setOnKeyListener true
            }
            false
        }
        btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showKeyboard(editText)
                showSearchFragment()
                btnFilter.visibility = View.GONE
                btnSearchThis.visibility = View.VISIBLE
                editText.visibility = View.VISIBLE
                toolbarTitle.visibility = View.GONE
                btnSearch.visibility = View.GONE
                btnSearchBack.visibility = View.VISIBLE

            } else {
                hideSearchFragment()
                btnSearchThis.visibility = View.GONE
                editText.visibility = View.GONE
                btnFilter.visibility = View.VISIBLE
                toolbarTitle.visibility = View.VISIBLE
                btnSearch.visibility = View.VISIBLE
                btnSearchBack.visibility = View.GONE
            }
        }

        return view
    }


    override fun updateImageBasedOnFilterCount(count: Int) {
        val btnFilter: ImageView = requireView().findViewById(R.id.btnFilter)
        val imageToDisplay = when (count) {
            1 -> R.drawable.point1
            2 -> R.drawable.point2
            3 -> R.drawable.point3
            4 -> R.drawable.point3
            else -> R.drawable.point
        }

        btnFilter.setImageResource(imageToDisplay)
    }

    private fun showKeyboard(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        editText.setSelection(0)
        editText.postDelayed({
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }, 100)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun showSearchFragment() {
        isSearchMode = true
    }

    override fun hideSearchFragment() {
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