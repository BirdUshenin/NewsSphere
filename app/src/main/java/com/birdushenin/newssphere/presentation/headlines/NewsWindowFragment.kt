package com.birdushenin.newssphere.presentation.headlines

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
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
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.birdushenin.newssphere.presentation.saved.SavedViewModel
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                binding.content.text = it.content

                Glide.with(binding.picNews)
                    .load(it.urlToImage)
                    .into(binding.picNews)

                val savedClass = SavedClass(
                    it.title,
                    it.url,
                    it.description,
                    it.source.name,
                    it.publishedAt,
                    it.urlToImage
                )

                binding.content.setOnClickListener {
                    val url = savedClass.urlText // Assuming savedClass.urlText contains the URL you want to open
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }

                val contentTextView = binding.content
                val contentString = it.content
                val spannable = SpannableString(contentString)
                contentString?.let { it1 -> spannable.setSpan(UnderlineSpan(), 0, it1.length, 0) }
                contentTextView.text = spannable


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