package com.birdushenin.newssphere.presentation.saved

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.databinding.FragmentSavedWindowBinding
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.launch

class SavedWindowFragment : Fragment(), FragmentScreen {

    private val savedWindowViewModel: SavedWindowViewModel by activityViewModels()
    private val savedViewModel: SavedViewModel by activityViewModels()

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SavedWindowFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSavedWindowBinding.inflate(layoutInflater)

        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT

        savedWindowViewModel.selectedArticle.observe(viewLifecycleOwner) { article ->
            article?.let {
                binding.mainText.title = it.titleText
                binding.name.text = it.titleText
                binding.data.text = it.publishedAt
                binding.description.text = it.descriptionText
                binding.source.text = it.sourceText

                Glide.with(binding.picNews)
                    .load(it.imagePic)
                    .into(binding.picNews)


                val savedClass = SavedClass(
                    it.titleText,
                    it.urlText,
                    it.descriptionText,
                    it.sourceText,
                    it.publishedAt,
                    it.urlText
                )

                lifecycleScope.launch {
                    val savedNewsEntity = savedViewModel.savedArticleDao.getSavedNewsByTitleAndUrl(
                        savedClass.titleText,
                        savedClass.urlText
                    )
                    val imageResource = if (savedNewsEntity != null) {
                        R.drawable.ic_saved
                    } else {
                        R.drawable.baseline_save
                    }
                    binding.savedButton.setImageResource(imageResource)
                }

                binding.savedButton.setOnClickListener {
                    lifecycleScope.launch {
                        val savedNewsEntity =
                            savedViewModel.savedArticleDao.getSavedNewsByTitleAndUrl(
                                savedClass.titleText,
                                savedClass.urlText
                            )
                        if (savedNewsEntity != null) {
                            binding.savedButton.setImageResource(R.drawable.baseline_save)
                            savedViewModel.deleteArticle(savedClass)
                            Toast.makeText(context, "News removed from saved", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            binding.savedButton.setImageResource(R.drawable.ic_saved)
                            savedViewModel.selectArticle(savedClass)
                            Toast.makeText(context, "News saved", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                binding.backButton.setOnClickListener {
                    activity?.onBackPressed()
                }
            }
        }
        return binding.root
    }
}