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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT

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

                fun updateButtonImage() {
                    val imageResource = if (savedViewModel.isArticleSaved) {
                        R.drawable.ic_saved
                    } else {
                        R.drawable.baseline_save
                    }
                    binding.savedButton.setImageResource(imageResource)
                }

                binding.savedButton.setOnClickListener {
                    if (!savedViewModel.isArticleSaved){
                        savedViewModel.selectArticle(savedClass)
                        savedViewModel.isArticleSaved = true
                    } else {
                        savedViewModel.deleteArticle(savedClass)
                        savedViewModel.isArticleSaved = false
                    }
                    updateButtonImage()
                }

                binding.backButton.setOnClickListener {
                    activity?.onBackPressed()
                }
            }
        }

        return binding.root
    }
}