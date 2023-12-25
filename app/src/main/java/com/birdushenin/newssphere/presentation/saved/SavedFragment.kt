package com.birdushenin.newssphere.presentation.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.databinding.FragmentSavedBinding
import com.birdushenin.newssphere.presentation.adapters.SavedAdapter
import com.github.terrakok.cicerone.androidx.FragmentScreen

class SavedFragment : Fragment(), FragmentScreen {

    private val adapter = SavedAdapter()
    private val savedViewModel: SavedViewModel by activityViewModels()

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SavedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSavedBinding.inflate(layoutInflater)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        savedViewModel.selectedArticle.observe(
            viewLifecycleOwner, Observer { savedArticleList ->
                val savedClassList = savedArticleList.map { savedArticle ->
                    SavedClass(
                        titleText = savedArticle.titleText,
                        urlText = savedArticle.urlText,
                        descriptionText = savedArticle.descriptionText,
                        sourceText = savedArticle.sourceText,
                        imagePic = savedArticle.imagePic
                    )
                }
                adapter.submitList(savedClassList)
            }
        )

        // TODO загрузка изображений, окно

        return binding.root
    }
}