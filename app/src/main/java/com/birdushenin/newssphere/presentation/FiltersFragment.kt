package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.databinding.FragmentFiltersBinding
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FiltersFragment : Fragment(), FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment {
        return FiltersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFiltersBinding.inflate(layoutInflater)

        val radioGroup = binding.radioButton
        val filterViewModel: FilterViewModel by activityViewModels()

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLeft -> filterViewModel.setFilter("popular")
                R.id.rbCenter -> filterViewModel.setFilter("publishedAt")
                R.id.rbRight -> filterViewModel.setFilter("relevancy")
            }
        }

        val applyButton = binding.btnCheck
        applyButton.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }
}