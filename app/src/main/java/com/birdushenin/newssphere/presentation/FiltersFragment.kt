package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.databinding.FragmentFiltersBinding
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FiltersFragment : Fragment(), FragmentScreen {

    private lateinit var radioGroup: RadioGroup
    private var selectedFilter: String? = null
    private var tempSelectedFilter: String? = null

    override fun createFragment(factory: FragmentFactory): Fragment {
        return FiltersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFiltersBinding.inflate(layoutInflater)
        radioGroup = binding.radioButton
        val applyButton = binding.btnCheck
        val btnBack = binding.btnBack
        val filterViewModel: FilterViewModel by activityViewModels()

        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        filterViewModel.selectedFilter.observe(viewLifecycleOwner, Observer { selectedFilter ->
            restoreSelectedFilter(selectedFilter)
        })

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            tempSelectedFilter = when (checkedId) {
                R.id.rbLeft -> "popular"
                R.id.rbCenter -> "publishedAt"
                R.id.rbRight -> "relevancy"
                else -> null
            }
        }

        applyButton.setOnClickListener {
            tempSelectedFilter?.let {
                selectedFilter = it
                filterViewModel.setFilter(it)
            }

            activity?.onBackPressed()
        }
        return binding.root
    }

    private fun restoreSelectedFilter(savedFilter: String?) {
        when (savedFilter) {
            "popular" -> radioGroup.check(R.id.rbLeft)
            "publishedAt" -> radioGroup.check(R.id.rbCenter)
            "relevancy" -> radioGroup.check(R.id.rbRight)
        }
    }
}