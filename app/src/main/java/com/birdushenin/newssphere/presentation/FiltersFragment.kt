package com.birdushenin.newssphere.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
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

        return binding.root
    }

}