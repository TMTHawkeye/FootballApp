package com.example.footballapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.activities.NewsDetailActivity
import com.example.footballapp.adapters.newsadapters.NewsAdapter
import com.example.footballapp.adapters.newsadapters.SliderAdapter
import com.example.footballapp.databinding.FragmentNewsBinding
import com.example.footballapp.utils.DepthPageTransformer
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private  var sliderAdapter: SliderAdapter?=null
    private  var newsAdapter: NewsAdapter?=null

    val viewModel : FootballViewModel by activityViewModel()
    val teamViewModel : TeamViewmodel by activityViewModel()

    private val handler = Handler(Looper.getMainLooper())
    private  var autoSlideRunnable: Runnable?=null
    private var isAutoSliding = false

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
//        observeNewsData()
        observePagedNews()
       /* binding.refreshNews.setOnClickListener {
            viewModel.loadLatestNews("1")
        }*/

        binding.refreshNews.setOnClickListener {
            showLoading(true)
            binding.textView4
            newsAdapter?.refresh()
        }


    }

    private fun setupSlider() {

        sliderAdapter = SliderAdapter(mutableListOf())
         binding.newsSliderViewPager.apply {
            adapter = sliderAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            setPageTransformer(DepthPageTransformer())
        }

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
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


/*    private fun observePagedNews() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Feed for RecyclerView
                viewModel.latestPagedNewsFlow.collectLatest { pagingData ->
                    newsAdapter?.submitData(pagingData)
                    sliderAdapter?.submitData(pagingData)
                    setSliderPageChangeCallback()
                }
            }
        }

    }*/

 /*   private fun observePagedNews() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.latestPagedNewsFlow.collectLatest { pagingData ->
                    newsAdapter?.submitData(pagingData)

                    // Collect first snapshot from paging to populate slider
                    newsAdapter?.let { adapter ->
                        lifecycleScope.launch {
                            adapter.loadStateFlow.collectLatest { state ->
                                val snapshot = adapter.snapshot().items.take(5) // top 5 news
                                sliderAdapter?.updateList(snapshot)
                                setSliderPageChangeCallback(snapshot)
                            }
                        }
                    }
                }
            }
        }
    }*/

  /*  private fun observePagedNews() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter?.let { adapter ->
                    launch {
                        adapter.loadStateFlow.collectLatest { loadStates ->
                            val isListReady =
                                loadStates.refresh is androidx.paging.LoadState.NotLoading &&
                                        adapter.itemCount > 0

                            Log.d("NewsFragment", "Slider isListReady = $isListReady, itemCount=${adapter.itemCount}")

                            if (isListReady) {
                                val snapshot = adapter.snapshot().items.take(5)
                                Log.d("NewsFragment", "Slider snapshot size: ${snapshot.size}")

                                if (snapshot.isNotEmpty()) {
                                    sliderAdapter?.updateList(snapshot)
                                    setSliderPageChangeCallback(snapshot)
                                    setupSliderIndicator()
                                }
                            }
                        }
                    }
                }

                viewModel.latestPagedNewsFlow.collectLatest { pagingData ->
                    newsAdapter?.submitData(pagingData)
                }
            }
        }
    }

*/


    private fun observePagedNews() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Observe adapter load states for loading UI handling
                newsAdapter?.let { adapter ->
                    launch {
                        adapter.loadStateFlow.collectLatest { loadStates ->
                            val isLoading =
                                loadStates.refresh is androidx.paging.LoadState.Loading ||
                                        loadStates.append is androidx.paging.LoadState.Loading

                            val isError = loadStates.refresh is androidx.paging.LoadState.Error
                            val isLoaded =
                                loadStates.refresh is androidx.paging.LoadState.NotLoading &&
                                        adapter.itemCount > 0

                            Log.d("NewsFragment", "isLoading=$isLoading, isLoaded=$isLoaded, isError=$isError")

                            // Update shimmer loading state
                            showLoading(isLoading)

                            if (isLoaded) {
                                val snapshot = adapter.snapshot().items.take(5)
                                if (snapshot.isNotEmpty()) {
                                    sliderAdapter?.updateList(snapshot)
                                    setSliderPageChangeCallback(snapshot)
                                    setupSliderIndicator()
                                }
                            }
                        }
                    }
                }

                // Collect PagingData from ViewModel
                viewModel.latestPagedNewsFlow.collectLatest { pagingData ->
                    showLoading(true) // show shimmer while loading new data
                    newsAdapter?.submitData(pagingData)
                }
            }
        }
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
                binding.textView4Shimmer.visible()
                binding.refreshNewsShimmer.visible()
                binding.tvNewsTitleShimmer.visible()
                binding.tvNewsDateShimmer.visible()
                binding.indicatorShimmer.visible()
                binding.textView2Shimmer.visible()
                binding.rvLatestNewsShimmer.visible()


                binding.textView4.invisible()
                binding.refreshNews.invisible()
                binding.tvNewsTitle.invisible()
                binding.tvNewsDate.invisible()
                binding.indicator.invisible()
                binding.textView2.invisible()
                binding.rvLatestNews.invisible()
                binding.newsSliderViewPager.invisible()

            } else {

                binding.textView4Shimmer.gone()
                binding.refreshNewsShimmer.gone()
                binding.tvNewsTitleShimmer.gone()
                binding.tvNewsDateShimmer.gone()
                binding.indicatorShimmer.gone()
                binding.textView2Shimmer.gone()
                binding.rvLatestNewsShimmer.gone()

                binding.textView4.visible()
                binding.refreshNews.visible()
                binding.tvNewsTitle.visible()
                binding.tvNewsDate.visible()
                binding.indicator.visible()
                binding.textView2.visible()
                binding.rvLatestNews.visible()
                binding.newsSliderViewPager.visible()
            }
        } ?: run {
        }
    }

    private fun setupSliderIndicator() {
        val indicator = binding.indicator
        indicator.setViewPager(binding.newsSliderViewPager)
        autoSlideNews()
    }


    fun autoSlideNews() {
        if (isAutoSliding ) return // prevent multiple starts

        isAutoSliding = true
        autoSlideRunnable = object : Runnable {
            override fun run() {
                val viewPager = binding.newsSliderViewPager
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem == viewPager.adapter?.itemCount?.minus(1)) 0 else currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
//        handler.removeCallbacksAndMessages(null)

        autoSlideRunnable?.let { handler.postDelayed(it, 3000) }
    }
    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSlide()
    }

    private fun stopAutoSlide() {
        autoSlideRunnable?.let {
            handler.removeCallbacks(it)
        }
        isAutoSliding = false
    }
}
