package com.birdushenin.newssphere.presentation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen

class NewsWindowFragment : Fragment(), FragmentScreen {

    private val sharedViewModel: NewsViewModel by activityViewModels()
    private val savedViewModel: SavedViewModel by activityViewModels()

    override fun createFragment(factory: FragmentFactory): Fragment {
        return NewsWindowFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewsWindowBinding.inflate(layoutInflater)
//        (binding.toolbar as? Toolbar)?.let {
//            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

        sharedViewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.mainText.title = it.title
                binding.name.text = it.title
                binding.data.text = it.publishedAt
                binding.description.text = it.description
                binding.source.text = it.source.name

                Glide.with(binding.picNews)
                    .load(it.urlToImage)
                    .into(binding.picNews)

                val savedClass = SavedClass(
                    it.title,
                    it.url,
                    it.description,
                    it.source.name,
                    it.urlToImage
                )

                binding.savedButton.setOnClickListener {
                    savedViewModel.selectArticle(savedClass)
                    Log.d("SavedViewModel", "Inserting saved article savedClass: $savedClass")
                }

                binding.backButton.setOnClickListener {
                    activity?.onBackPressed()
                }
            }
        }
        return binding.root
    }
}