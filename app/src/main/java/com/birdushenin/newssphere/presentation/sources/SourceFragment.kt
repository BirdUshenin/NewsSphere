package com.birdushenin.newssphere.presentation.sources

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.SourceNews
import com.birdushenin.newssphere.data.databases.daos.SourceDao
import com.birdushenin.newssphere.databinding.FragmentSourceBinding
import com.birdushenin.newssphere.domain.OnSourceItemClickListener
import com.birdushenin.newssphere.navigation.HeadlinesScreens
import com.birdushenin.newssphere.navigation.SavedNavigation
import com.birdushenin.newssphere.presentation.adapters.SourceAdapter
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class SourceFragment : Fragment(), FragmentScreen {

    private lateinit var binding: FragmentSourceBinding

    private var isSearchMode = false
    private val adapter = SourceAdapter()
    private val sourceViewModel: SourceViewModel by activityViewModels()
    private val sourceGlobalViewModel: SourceGlobalViewModel by activityViewModels() {
        MyApplication.appComponent.sourceViewModelFactory()
    }

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var sourceNewsDao: SourceDao

    @Inject
    lateinit var router: Router

    override fun createFragment(factory: FragmentFactory): Fragment {
        return SourceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSourceBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        binding.progressBar.visibility = View.VISIBLE
        val editText = binding.editSearch

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        binding.btnSearch.setOnClickListener {
            if (!isSearchMode) {
                showKeyboardAndFocus(editText)
                editText.requestFocus()
                Handler(Looper.getMainLooper()).postDelayed({
                    editText.requestFocus()
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }, 200)
                showSearchFragment()
                editText.isFocusableInTouchMode = true
                binding.btnSearch.visibility = View.GONE
                binding.toolbarTitle.visibility = View.GONE
                editText.visibility = View.VISIBLE
                binding.btnSearchThis.visibility = View.VISIBLE
                binding.btnSearchBack.visibility = View.VISIBLE
            } else {
                hideSearchFragment()
                binding.btnSearch.visibility = View.VISIBLE
                binding.toolbarTitle.visibility = View.VISIBLE
                editText.visibility = View.GONE
                binding.btnSearchThis.visibility = View.GONE
                binding.btnSearchBack.visibility = View.GONE
            }
        }

        binding.btnSearchThis.setOnClickListener {
            editText.text.clear()
        }

        binding.btnSearchBack.setOnClickListener {
            router.navigateTo(SavedNavigation.SourceFragment())
            hideKeyboard()
        }

        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = editText.text.toString()
                lifecycleScope.launch {
                    searchSource(query)
                }
                return@setOnKeyListener true
            }
            false
        }

        sourceGlobalViewModel.news.observe(viewLifecycleOwner) { newsList ->
            if (newsList.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
                adapter.submitList(newsList)
            } else {
                binding.refresh.visibility = View.VISIBLE
                binding.refreshPic.visibility = View.VISIBLE
                binding.notInternet.visibility = View.VISIBLE
                binding.notInternetPic.visibility = View.VISIBLE
            }
        }

        binding.refreshPic.setOnClickListener {
            lifecycleScope.launch {
                sourceGlobalViewModel.loadNews()
            }
            binding.notInternet.text = "Something went wrong Try later"
            Toast.makeText(context, "Not internet connection", Toast.LENGTH_SHORT)
                .show()
        }

        adapter.setOnUserItemClickListener(object : OnSourceItemClickListener {
            override fun onNewsItemClicked(sourceNews: SourceNews) {
                sourceViewModel.selectSource(sourceNews)
                (requireActivity().application as MyApplication).router.navigateTo(HeadlinesScreens.SourceWindowFragment)
            }
        })

        lifecycleScope.launch {
            sourceGlobalViewModel.loadNews()
        }

        return binding.root
    }

    private fun showKeyboardAndFocus(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun showSearchFragment() {
        isSearchMode = true
    }

    private fun hideSearchFragment() {
        isSearchMode = false
    }

    private suspend fun searchSource(query: String) {
        val searchResults: List<SourceNews>
        val offlineArticles = sourceNewsDao.searchArticles(query)
        searchResults = offlineArticles.map { articleEntity ->
            SourceNews(
                name = articleEntity.name,
                description = articleEntity.description,
                country = articleEntity.country,
                category = articleEntity.category,
                urlToImage = articleEntity.urlToImage,
                url = articleEntity.url,
                id = articleEntity.sourceId
            )
        }
        withContext(Dispatchers.Main) {
            adapter.submitList(searchResults)
        }
    }
}