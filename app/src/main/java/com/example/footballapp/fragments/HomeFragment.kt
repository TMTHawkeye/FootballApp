package com.example.footballapp.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.AllMatchesActivity
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.activities.onboarding.SearchHomeActivity
import com.example.footballapp.activities.onboarding.SettingActivity
import com.example.footballapp.adapters.StageAdapter
import com.example.footballapp.adapters.matchadapters.DateAdapter
 import com.example.footballapp.adapters.matchadapters.MatchSliderAdapter
import com.example.footballapp.databinding.FragmentHomeBinding
import com.example.footballapp.interfaces.OnMatchSelected
import com.example.footballapp.interfaces.OnStageClickListener
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.models.matchmodels.MatchDate
import com.example.footballapp.utils.DepthPageTransformer
import com.example.footballapp.viewmodels.MatchViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class HomeFragment : Fragment(), OnStageClickListener, OnMatchSelected {
    lateinit var binding: FragmentHomeBinding
    private lateinit var dateAdapter: DateAdapter
    private lateinit var matchSliderAdapter: MatchSliderAdapter

    private val dates = mutableListOf<MatchDate>()
    private val matchesByDate = mutableMapOf<String, List<Match>>()
    private var currentDate: String = ""

    private val viewModel: FootballViewModel by activityViewModel()
    private val teamViewModel: TeamViewmodel by activityViewModel()
    var formattedDate: String? = null
    var stageAdapter: StageAdapter? = null

    private val matchViewModel: MatchViewModel by viewModel()


    private var allStages: List<Stage> = emptyList()
    private val pageSize = 20
    private var currentPage = 1
    private var isLoadingMore = false
    private val handler = Handler(Looper.getMainLooper())
    private  var autoSlideRunnable: Runnable?=null
    private var isAutoSliding = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        setupDateRecyclerView()
        setUpCompetitionRV()
        observeCompetitions()

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }

        // Add click listener for "See All" button


        binding.btnHistory.setOnClickListener {
            viewModel.loadMatchesWithStages(formattedDate)
        }

//        setupMatchSlider()

        setupNavigationButtons()

        binding.btnSearch.setOnClickListener {
            val context = requireContext()
            val intent = Intent(context, SearchHomeActivity::class.java)

            val options = ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right,  // enter animation
                R.anim.slide_out_left   // exit animation
            )

            startActivity(intent, options.toBundle())
        }
    }



    private fun setupDateRecyclerView() {
         val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM, EEE")
        val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val initialDates = (-7..7).map { offset ->
            val date = today.plusDays(offset.toLong())
            MatchDate(
                displayText = date.format(formatter),
                fullDate = date.format(backendFormatter),
                isSelected = offset == 0
            )
        }.toMutableList()

        dateAdapter = DateAdapter(initialDates) { selectedDate ->
            formattedDate =
                LocalDate.parse(selectedDate.fullDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"))

            Log.d("TAG_DATE", "Selected Date: $formattedDate")

            viewModel.loadMatchesWithStages(formattedDate)

        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.datesRecyclerView.apply {
            this.layoutManager = layoutManager
            adapter = dateAdapter
        }

        // Snap to center
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.datesRecyclerView)

        // Scroll listener for pagination
        binding.datesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if (firstVisible <= 2) {
                    prependDates()
                } else if (lastVisible >= dateAdapter.itemCount - 3) {
                    appendDates()
                }
            }
        })

        // Start with today in the center
        val todayPos = initialDates.indexOfFirst { it.isSelected }
        binding.datesRecyclerView.scrollToPosition(todayPos)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prependDates() {
        val firstDate = LocalDate.parse(dateAdapter.dates.first().fullDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM, EEE")
        val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val newDates = (1..7).map { offset ->
            val date = firstDate.minusDays(offset.toLong())
            MatchDate(date.format(formatter), date.format(backendFormatter))
        }.reversed()

        dateAdapter.addDates(newDates, atStart = true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun appendDates() {
        val lastDate = LocalDate.parse(dateAdapter.dates.last().fullDate)
        val formatter = DateTimeFormatter.ofPattern("dd MMM, EEE")
        val backendFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val newDates = (1..7).map { offset ->
            val date = lastDate.plusDays(offset.toLong())
            MatchDate(date.format(formatter), date.format(backendFormatter))
        }

        dateAdapter.addDates(newDates, atStart = false)
    }


    private fun setupMatchSlider(liveMatches: List<Matche>) {
        matchSliderAdapter = MatchSliderAdapter(liveMatches) { match ->
            onMatchClicked(match)
            // Start MatchDetailActivity when a match is clicked
            val intent = Intent(requireContext(), MatchDetailActivity::class.java)
//            intent.putExtra("MATCH_DATA", match as Parcelable)
            startActivity(intent)
        }

        binding.matchSliderViewPager.apply {
            adapter = matchSliderAdapter
            offscreenPageLimit = 3


            setPageTransformer(DepthPageTransformer())
        }
    }


    private fun setupNavigationButtons() {
        binding.btnPrevious.setOnClickListener {
            val currentPosition = binding.matchSliderViewPager.currentItem
            if (currentPosition > 0) {
                binding.matchSliderViewPager.setCurrentItem(currentPosition - 1, true)
            }
        }

        binding.btnNext.setOnClickListener {
            val currentPosition = binding.matchSliderViewPager.currentItem
            val totalItems = matchSliderAdapter.itemCount
            if (currentPosition < totalItems - 1) {
                binding.matchSliderViewPager.setCurrentItem(currentPosition + 1, true)
            }
        }

        binding.matchSliderViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavButtons()
            }
        })

    }

    private fun updateNavButtons() {
        val currentPosition = binding.matchSliderViewPager.currentItem
        val totalItems = matchSliderAdapter.itemCount

        // Disable prev if at first item
        binding.btnPrevious.isEnabled = currentPosition > 0

        // Disable next if at last item
        binding.btnNext.isEnabled = currentPosition < totalItems - 1
    }



    private fun loadNextPage() {
        if (isLoadingMore) return
        isLoadingMore = true

        val startIndex = currentPage * pageSize
        val endIndex = (startIndex + pageSize).coerceAtMost(allStages.size)

        if (startIndex < allStages.size) {
            val nextPageData = allStages.subList(startIndex, endIndex)

            // Simulate loading delay if needed
            lifecycleScope.launch(Dispatchers.Main) {
                // Optional: show footer shimmer or progress here

                stageAdapter?.addMoreStages(nextPageData)
                currentPage++
                isLoadingMore = false

                Log.d("TAG_loadstages", "loadNextPage: morestages added")
            }
        } else {
            isLoadingMore = false
        }
    }


    private fun setUpCompetitionRV() {
//        binding.matchesRecyclerView.isNestedScrollingEnabled = false
        val layoutManager = LinearLayoutManager(requireContext())
        binding.matchesRecyclerView.layoutManager = layoutManager
        stageAdapter = StageAdapter(mutableListOf(), this@HomeFragment, this@HomeFragment){league->
            arrangeLeagueModel(league)

        }
        binding.matchesRecyclerView.adapter = stageAdapter


        // Pagination listener
        binding.matchesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val threshold = totalItemCount - 10
                if (!isLoadingMore && lastVisible >= threshold) {
                    loadNextPage()
                }
            }
        })




    }



    private fun observeCompetitions() {
        this@HomeFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matchesFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            showLoading(false)
                             withContext(Dispatchers.Main) {
                                updateCompetitions(result.data)
                            }


                            withContext(Dispatchers.IO) {

                                val todayFormatted =
                                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                                Log.d("TAG_today", "observeCompetitions: $formattedDate")
                                val isToday = formattedDate == todayFormatted
                                val processedData = withContext(Dispatchers.Default) {
                                    if (isToday) result.data else null
                                }

                                 withContext(Dispatchers.Main) {
                                    if (isToday) {
                                        setLiveMAtchesList(processedData)
                                        binding.matchesHeadingTv.text =
                                            binding.root.context.getString(R.string.other_today_s_matches)
                                    } else {
                                        binding.matchesHeadingTv.text =
                                            binding.root.context.getString(R.string.other_matches)
                                    }
                                }
                            }
                        }

                        is ApiResult.Error -> {

                            showLoading(null)
                            showError(result.throwable)
                        }
                    }
                }
            }
        }
    }


  /*  private fun updateCompetitions(data: List<Stage>) {
        stageAdapter?.updateData(data)
    }*/

    private fun updateCompetitions(data: List<Stage>) {
        allStages = data
        currentPage = 1
        val initialList = data.take(pageSize).toMutableList()
        stageAdapter?.updateData(initialList)
    }


    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {
                binding.ctShimmers.visible()
                binding.textView3shimmer.visible()
                binding.matchesHeadingTvshimmer.visible()
                binding.ctSliderShimmer.visible()
                binding.datesRecyclerViewshimmer.visible()
                binding.seeAllShimmer.visible()


                binding.datesRecyclerView.invisible()
                binding.matchesRecyclerView.invisible()
                binding.matchSliderViewPager.invisible()
                binding.textView3.invisible()
                binding.matchesHeadingTv.invisible()

                binding.btnPrevious.invisible()
                binding.btnNext.invisible()
                binding.seeAll.invisible()

            } else {
                binding.ctShimmers.gone()
                binding.textView3shimmer.gone()
                binding.matchesHeadingTvshimmer.gone()
                binding.ctSliderShimmer.gone()
                binding.datesRecyclerViewshimmer.gone()
                binding.seeAllShimmer.gone()

                binding.datesRecyclerView.visible()
                binding.matchesRecyclerView.visible()
                binding.matchSliderViewPager.visible()
                binding.textView3.visible()
                binding.matchesHeadingTv.visible()
                binding.btnPrevious.visible()
                binding.btnNext.visible()
                binding.seeAll.visible()
            }
        }?:run{
            binding.ctShimmers.gone()
            binding.textView3shimmer.gone()
            binding.matchesHeadingTvshimmer.gone()
            binding.ctSliderShimmer.gone()
            binding.datesRecyclerViewshimmer.gone()
            binding.seeAllShimmer.gone()

            binding.datesRecyclerView.invisible()
            binding.matchesRecyclerView.invisible()
            binding.matchSliderViewPager.invisible()
            binding.textView3.invisible()
            binding.matchesHeadingTv.invisible()
            binding.btnPrevious.invisible()
            binding.btnNext.invisible()
            binding.seeAll.invisible()
        }
    }

    private fun showError(throwable: Throwable) {
        Log.d(ApiResultTAG, "showError: ${throwable.message.toString()}")

        Toast.makeText(requireContext(), throwable.message ?: "Error", Toast.LENGTH_SHORT).show()
    }


    override fun onStageExpanded(stage: Stage) {
    }

    fun setLiveMAtchesList(matchesList: List<Stage>?) {
            val liveMatches = matchesList.orEmpty()
                .flatMap { stage ->
                    stage.matches.orEmpty().map { match ->
                        match.tournamentLogo = stage.badge_url
                        match.tournamentName = stage.stage_name
                        match.copy(
                            tournamentLogo = stage.badge_url?.takeIf { it != "null" }
                                ?: match.tournamentLogo,
                            tournamentName =
                                adjustNameForTournament(stage)
                        )
                    }
                }
                .filter { match -> match.match_status?.contains("'") == true }
        Log.d("TAG_setLiveMAtchesList", "setLiveMAtchesList: ${liveMatches.size}")

        if(liveMatches.size!=0) {
            autoSlideLiveMatches(liveMatches)
            if(liveMatches?.size==1){
                binding.seeAll.invisible()
            }
            else{
                binding.seeAll.visible()
            }
            binding.relativeLayout.visible()
            binding.textView3.visible()
            binding.textView3.text = getString(R.string.livematches) + " ${liveMatches.size}"
            setupMatchSlider(liveMatches)
        }
        else{
            binding.relativeLayout.gone()
            binding.seeAll.invisible()
            binding.textView3.text = getString(R.string.livematches) + " ${liveMatches.size}"

//            binding.textView3.gone()
        }

        binding.seeAll.setOnClickListener {
            teamViewModel.setLiveMatches(liveMatches)
//            val allMatches = getAllMatches()
            val intent = Intent(requireContext(), AllMatchesActivity::class.java)
//            intent.putParcelableArrayListExtra("MATCH_DATA", ArrayList(allMatches))
            startActivity(intent)
        }
    }

    fun adjustNameForTournament(stage : Stage) : String{
        val compName = stage.competition_name?.takeIf { it != "null" } ?: ""
        val stageName = stage.stage_name?.takeIf { it != "null" } ?: ""
        val displayName = when {
            compName.equals(stageName, ignoreCase = true) -> compName // if both same
            compName.isNotBlank() && stageName.isNotBlank() -> "$compName - $stageName"
            compName.isNotBlank() -> compName
            stageName.isNotBlank() -> stageName
            else -> "Unknown Competition"
        }

        return displayName
    }

    override fun onMatchClicked(matche: Matche) {
        Log.d("TAG_MATCHDATA", "onMatchClicked: ${matche}")
//        matche.tournamentLogo = stageBadge
        matchViewModel.setMatch(matche)
    }


   /* fun autoSlideLiveMatches(liveMatches: List<Matche>) {
        // Auto-slide functionality

        autoSlideRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.matchSliderViewPager.currentItem
                val nextItem = if (currentItem == liveMatches.size - 1) 0 else currentItem + 1
                binding.matchSliderViewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(autoSlideRunnable, 3000)
    }
*/

    fun autoSlideLiveMatches(liveMatches: List<Matche>) {
        if (isAutoSliding || liveMatches.isEmpty()) return // prevent multiple starts

        isAutoSliding = true
        autoSlideRunnable = object : Runnable {
            override fun run() {
                val viewPager = binding.matchSliderViewPager
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem == liveMatches.size - 1) 0 else currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 5000)
            }
        }
//        handler.removeCallbacksAndMessages(null)

        autoSlideRunnable?.let { handler.postDelayed(it, 5000) }
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

    private fun selectLeague(league : com.example.footballapi.modelClasses.AllCompetitions.Stage){
        teamViewModel.setLeague(league)
        binding.root.context.startActivity(
            Intent(
                binding.root.context,
                LeagueDetailActivity::class.java
            )
        )


    }

    private fun arrangeLeagueModel(league : Stage){
        val stage = com.example.footballapi.modelClasses.AllCompetitions.Stage(
            stage_id = league.stage_id,
            competition_id = league.competition_id,
            stage_name = league.stage_name,
            competition_name = league.competition_name,
            badge_url = league.badge_url,
             competition_desc = league.competition_description,
            competition_url_name = league.competition_url_name,
            country_code = league.country_code,
            country_name = league.country_name,
            first_color = league.primary_color,
            stage_code = league.stage_code,
            stage_short = league.country_short_name
             )

        selectLeague(stage)
    }
}