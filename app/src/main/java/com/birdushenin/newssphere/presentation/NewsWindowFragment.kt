package com.birdushenin.newssphere.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.birdushenin.newssphere.Application
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.Article
import com.birdushenin.newssphere.databinding.FragmentNewsWindowBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnNewsItemClickListener
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class NewsWindowFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewsWindowBinding.inflate(layoutInflater)

        val title = arguments?.getString("title") ?: ""
        val description = arguments?.getString("description")
        val urlToImage = arguments?.getString("urlToImage")
        val source = arguments?.getString("source")

        binding.mainText.text = title
        binding.description.text = description
        binding.source.text = source

        Glide.with(binding.picNews)
            .load(urlToImage)
            .into(binding.picNews)

        return binding.root
    }

    companion object {
        fun newInstance(title: String, description: String?, urlToImage: String?, source: String): NewsWindowFragment {
            val fragment = NewsWindowFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("description", description)
            args.putString("urlToImage", urlToImage)
            args.putString("source", source)
            fragment.arguments = args
            return fragment
        }
    }
}
