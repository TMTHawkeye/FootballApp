package com.example.footballapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.activities.NewsDetailActivity
import com.example.footballapp.adapters.newsadapters.NewsAdapter
import com.example.footballapp.adapters.newsadapters.SliderAdapter
import com.example.footballapp.databinding.FragmentHomeBinding
import com.example.footballapp.databinding.FragmentNewsBinding
import com.example.footballapp.models.newsmodel.NewsItem
import com.example.footballapp.models.newsmodel.SliderItem


class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var newsAdapter: NewsAdapter

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
    }

    private fun setupSlider() {
        val sliderItems = listOf(
            SliderItem(
                R.drawable.foot1,  // Drawable resource ID
                "Breaking News 1",
                "Today"
            ),
            SliderItem(
                R.drawable.app_icon,
                "Breaking News 2",
                "Yesterday"
            ),
            SliderItem(
                R.drawable.arrow_back,
                "Breaking News 3",
                "2 days ago"
            ),
            SliderItem(
                R.drawable.arrow_back,
                "Breaking News 4",
                "3 days ago"
            ),
            SliderItem(
                R.drawable.arrow_back,
                "Breaking News 5",
                "4 days ago"
            )
        )
        sliderAdapter = SliderAdapter(sliderItems)
        binding.newsSliderViewPager.adapter = sliderAdapter

        // Update text views based on current slider item
        binding.newsSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentItem = sliderItems[position]
                binding.tvNewsTitle.text = currentItem.title
                binding.tvNewsDate.text = currentItem.date
            }
        })
    }

    private fun setupRecyclerView() {
        val newsItems = listOf(
            NewsItem(
                R.drawable.foot1,
                "Latest News 1",
                getString(R.string.long_news_description_1),
                "1 hour ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 2",
                getString(R.string.long_news_description_2),
                "2 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 3",
                getString(R.string.long_news_description_3),
                "3 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 4",
                getString(R.string.long_news_description_4),
                "4 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 1",
                getString(R.string.long_news_description_1),
                "1 hour ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 2",
                getString(R.string.long_news_description_2),
                "2 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 3",
                getString(R.string.long_news_description_3),
                "3 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 4",
                getString(R.string.long_news_description_4),
                "4 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 1",
                getString(R.string.long_news_description_1),
                "1 hour ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 2",
                getString(R.string.long_news_description_2),
                "2 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 3",
                getString(R.string.long_news_description_3),
                "3 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 4",
                getString(R.string.long_news_description_4),
                "4 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 1",
                getString(R.string.long_news_description_1),
                "1 hour ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 2",
                getString(R.string.long_news_description_2),
                "2 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 3",
                getString(R.string.long_news_description_3),
                "3 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 4",
                getString(R.string.long_news_description_4),
                "4 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 1",
                getString(R.string.long_news_description_1),
                "1 hour ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 2",
                getString(R.string.long_news_description_2),
                "2 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 3",
                getString(R.string.long_news_description_3),
                "3 hours ago"
            ),
            NewsItem(
                R.drawable.foot1,
                "Latest News 4",
                getString(R.string.long_news_description_4),
                "4 hours ago"
            )
        )

        newsAdapter = NewsAdapter(newsItems)
        binding.rvLatestNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Set click listener
        newsAdapter.setOnItemClickListener { newsItem ->
            val intent = Intent(requireContext(), NewsDetailActivity::class.java).apply {
                putExtra("NEWS_ITEM", newsItem)
            }
            startActivity(intent)
        }
    }

    private fun setupSliderIndicator() {
        val indicator = binding.indicator
        indicator.setViewPager(binding.newsSliderViewPager)
    }
}
