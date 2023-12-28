package com.birdushenin.newssphere.presentation.headlines.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.birdushenin.newssphere.MyApplication
import com.birdushenin.newssphere.data.databases.daos.ArticleDao
import com.birdushenin.newssphere.databinding.FragmentGeneralBinding
import com.birdushenin.newssphere.mvp.GeneralPresenter
import com.birdushenin.newssphere.presentation.adapters.NewsAdapter
import com.birdushenin.newssphere.presentation.headlines.NewsViewModel
import com.birdushenin.newssphere.presentation.headlines.SearchViewModel
import com.birdushenin.newssphere.presentation.headlines.UpdateViewModel
import com.birdushenin.newssphere.presentation.headlines.filters.FilterViewModel
import retrofit2.Retrofit
import javax.inject.Inject

class GeneralFragment : Fragment() {

    private lateinit var binding: FragmentGeneralBinding

    val sharedViewModel: NewsViewModel by activityViewModels()
    private val updateViewModel: UpdateViewModel by activityViewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()
    private val generalViewModel: GeneralViewModel by activityViewModels {
        MyApplication.appComponent.viewModelsFactory()
    }

    val adapter = NewsAdapter()
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var articleDao: ArticleDao

    private val presenter by lazy {
        GeneralPresenter(
            generalViewModel,
            filterViewModel,
            searchViewModel,
            updateViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneralBinding.inflate(layoutInflater)
        MyApplication.appComponent.inject(this)

        presenter.attach(this, binding)

        return binding.root
    }
}