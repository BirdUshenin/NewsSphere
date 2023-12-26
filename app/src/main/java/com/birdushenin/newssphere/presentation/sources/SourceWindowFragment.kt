package com.birdushenin.newssphere.presentation.sources

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.databinding.FragmentSourceWindowBinding
import com.birdushenin.newssphere.domain.NewsService
import com.birdushenin.newssphere.presentation.adapters.NewsAdapter
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class SourceWindowFragment : Fragment(), FragmentScreen {

    private val sourceViewModel: SourceViewModel by activityViewModels()
    private val adapter = NewsAdapter()

    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var sourceNewsDao: SourceDao

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SourceWindowFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSourceWindowBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val newsService = retrofit.create(NewsService::class.java)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        sourceViewModel.selectedSource.observe (viewLifecycleOwner) { source ->
            source?.let {
                lifecycleScope.launch {
                    loadNews(newsService, it.id)
                    binding.toolbarTitle.text = it.name
                }
            }
        }

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        return binding.root
    }

    private suspend fun loadNews(newsService: NewsService, sources: String?) {
        val apiKey = "4897ed61df034fa4b4bb185141dfe043"

        try {
            val response = newsService.getSourcesNews(apiKey = apiKey, source = sources)

                val newsList = response.body()?.articles ?: emptyList()
                if (response.isSuccessful){
                withContext(Dispatchers.Main) {
                    adapter.submitList(newsList)
                }
            } else { }
        } catch (_: Exception) { }
    }
}