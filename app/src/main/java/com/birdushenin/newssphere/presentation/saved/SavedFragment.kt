package com.birdushenin.newssphere.presentation.saved

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.R
import com.birdushenin.newssphere.data.SavedClass
import com.birdushenin.newssphere.data.databases.daos.SavedNewsDao
import com.birdushenin.newssphere.databinding.FragmentSavedBinding
import com.birdushenin.newssphere.domain.OnSavedItemClickListener
import com.birdushenin.newssphere.navigation.SavedNavigation
import com.birdushenin.newssphere.presentation.adapters.SavedAdapter
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedFragment : Fragment(), FragmentScreen {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var savedNewsDao: SavedNewsDao

    private val adapter = SavedAdapter()
    private val savedViewModel: SavedViewModel by activityViewModels()
    private val savedWindowViewModel: SavedWindowViewModel by activityViewModels()
    private var isSearchMode = false

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SavedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSavedBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        val editText = binding.editSearch

        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.setOnUserItemClickListener(object : OnSavedItemClickListener {
            override fun onSavedItemClicked(savedClass: SavedClass) {
                savedWindowViewModel.selectArticle(savedClass)
                router.navigateTo(SavedNavigation.SavedWindowFragment())
            }
        })

        binding.btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showSearchFragment()
                binding.btnSearch.visibility = View.GONE
                editText.visibility = View.VISIBLE
                binding.toolbarTitle.visibility = View.GONE
                binding.btnSearchThis.visibility = View.VISIBLE
                binding.btnSearchBack.visibility = View.VISIBLE

            } else {
                hideSearchFragment()
                binding.btnSearch.visibility = View.VISIBLE
                editText.visibility = View.GONE
                binding.toolbarTitle.visibility = View.VISIBLE
                binding.btnSearchThis.visibility = View.GONE
                binding.btnSearchBack.visibility = View.GONE
            }
        }

        binding.btnSearchThis.setOnClickListener {
            editText.text.clear()
        }

        binding.btnSearchBack.setOnClickListener {
            router.navigateTo(SavedNavigation.SavedFragment())
        }

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = editText.text.toString()
                lifecycleScope.launch {
                    searchArticles(query)
                }
                return@setOnKeyListener true
            }
            false
        }

        lifecycleScope.launch {
            savedViewModel.selectedArticle.observe(
                viewLifecycleOwner, Observer { savedArticleList ->
                    val savedClassList = savedArticleList.map { savedArticle ->
                        SavedClass(
                            titleText = savedArticle.titleText,
                            urlText = savedArticle.urlText,
                            descriptionText = savedArticle.descriptionText,
                            sourceText = savedArticle.sourceText,
                            publishedAt = savedArticle.publishedAt,
                            imagePic = savedArticle.imagePic
                        )
                    }
                    adapter.submitList(savedClassList)
                }
            )
        }

        return binding.root
    }

    private fun showSearchFragment() {
        isSearchMode = true
    }

    private fun hideSearchFragment() {
        isSearchMode = false
    }

    private suspend fun searchArticles(query: String) {
        val searchResults: List<SavedClass>

        val offlineArticles = savedNewsDao.searchArticles(query)
        searchResults = offlineArticles.map { articleEntity ->
            SavedClass(
                sourceText = articleEntity.sourceText,
                titleText = articleEntity.titleText,
                descriptionText = articleEntity.descriptionText,
                urlText = articleEntity.urlText,
                imagePic = articleEntity.imagePic,
                publishedAt = articleEntity.publishedAt
            )
        }
        withContext(Dispatchers.Main) {
            adapter.submitList(searchResults)
        }
    }
}