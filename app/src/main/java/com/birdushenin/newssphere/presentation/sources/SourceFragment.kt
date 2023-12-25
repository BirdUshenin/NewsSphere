package com.birdushenin.newssphere.presentation.sources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.databinding.FragmentSourceBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.domain.OnSourceItemClickListener
import com.birdushenin.newssphere.navigation.Screens
import com.birdushenin.newssphere.presentation.adapters.SourceAdapter
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class SourceFragment : Fragment(), FragmentScreen {

    private val adapter = SourceAdapter()
    private val sourceViewModel: SourceViewModel by activityViewModels()

    @Inject
    lateinit var retrofit: Retrofit

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SourceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSourceBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnUserItemClickListener(object : OnSourceItemClickListener {
            override fun onNewsItemClicked(sourceNews: SourceNews) {
                sourceViewModel.selectSource(sourceNews)
                (requireActivity().application as MyApplication).router.navigateTo(Screens.SourceWindowFragment)
            }
        })

        lifecycleScope.launch {
            loadNews(newsService)
        }

        return binding.root
    }

    private suspend fun loadNews(newsService: NewsService) {
        val apiKey = "eae4e313c2d043c183e78149bc172501"

        try {
            val response = newsService.getSources(apiKey = apiKey)
            if (response.isSuccessful) {
                val newsList = response.body()?.sources ?: emptyList()
                withContext(Dispatchers.Main) {
                    adapter.submitList(newsList)
                }
            }
        } catch (_: Exception) {
        }
    }
}