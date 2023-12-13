package com.birdushenin.newssphere.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen

class NewsWindowFragment : Fragment(), FragmentScreen {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun createFragment(factory: FragmentFactory): Fragment {
        return NewsWindowFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewsWindowBinding.inflate(layoutInflater)

        sharedViewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.mainText.text = it.title
                binding.description.text = it.description
                binding.source.text = it.source.name

                Glide.with(binding.picNews)
                    .load(it.urlToImage)
                    .into(binding.picNews)
            }
        }
        return binding.root
    }
}