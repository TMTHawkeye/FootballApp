package com.example.footballapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.R
import com.example.footballapp.activities.NewsDetailActivity
import com.example.footballapp.adapters.newsadapters.NewsAdapter
import com.example.footballapp.adapters.newsadapters.SliderAdapter
import com.example.footballapp.databinding.FragmentHomeBinding
import com.example.footballapp.databinding.FragmentNewsBinding
import com.example.footballapp.models.newsmodel.NewsItem
import com.example.footballapp.models.newsmodel.SliderItem
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private  var sliderAdapter: SliderAdapter?=null
    private  var newsAdapter: NewsAdapter?=null

    val viewModel : FootballViewModel by activityViewModel()
    val teamViewModel : TeamViewmodel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSlider()
        setupRecyclerView()
        setupSliderIndicator()
        observeNewsData()

        binding.refreshNews.setOnClickListener {
            viewModel.loadLatestNews("1")
        }

     }

    private fun setupSlider() {

        sliderAdapter = SliderAdapter(mutableListOf())
        binding.newsSliderViewPager.adapter = sliderAdapter


    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(mutableListOf())
        binding.rvLatestNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        // Set click listener
        newsAdapter?.setOnItemClickListener { newsItem ->
            teamViewModel.setSelectedNews(newsItem)
            val intent = Intent(requireContext(), NewsDetailActivity::class.java).apply {
//                putExtra("NEWS_ITEM", newsItem)
            }
            startActivity(intent)

        }
    }

    private fun observeNewsData() {
        this@NewsFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestNewsFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            showLoading(false)
                            val newsList = result.data
                            Log.d("TAG_newsList", "observeNewsData: ${newsList.size}")
                            newsAdapter?.updateData(newsList)
                            sliderAdapter?.updateList(newsList)
                            setSliderPageChangeCallback(newsList)
                        }

                        is ApiResult.Error -> {

                            showLoading(null)
                        }
                    }
                }
            }
        }

        viewModel.loadLatestNews("1")

    }

    fun setSliderPageChangeCallback(sliderItems : List<LatestNewsResponseItem>){
        // Update text views based on current slider item
        binding.newsSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItem = sliderItems[position]
                binding.tvNewsTitle.text = currentItem.title
                binding.tvNewsDate.text = currentItem.publish_time
            }
        })
    }
    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {


            } else {

            }
        } ?: run {
        }
    }

    private fun setupSliderIndicator() {
        val indicator = binding.indicator
        indicator.setViewPager(binding.newsSliderViewPager)
    }
}
