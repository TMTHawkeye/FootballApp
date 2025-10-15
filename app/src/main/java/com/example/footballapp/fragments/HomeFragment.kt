package com.example.footballapp.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
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
import com.example.footballapp.activities.onboarding.MainActivity
import com.example.footballapp.activities.onboarding.SettingActivity
import com.example.footballapp.adapters.StageAdapter
import com.example.footballapp.adapters.matchadapters.DateAdapter
import com.example.footballapp.adapters.matchadapters.MatchListAdapter
import com.example.footballapp.adapters.matchadapters.MatchSliderAdapter
import com.example.footballapp.databinding.FragmentHomeBinding
import com.example.footballapp.interfaces.OnMatchSelected
import com.example.footballapp.interfaces.OnStageClickListener
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.models.matchmodels.MatchDate
import com.example.footballapp.viewmodels.MatchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class HomeFragment : Fragment(), OnStageClickListener, OnMatchSelected {
    lateinit var binding: FragmentHomeBinding
    private lateinit var dateAdapter: DateAdapter
    private lateinit var matchSliderAdapter: MatchSliderAdapter
    private lateinit var matchListAdapter: MatchListAdapter

    private val dates = mutableListOf<MatchDate>()
    private val matchesByDate = mutableMapOf<String, List<Match>>()
    private var currentDate: String = ""

    private val viewModel: FootballViewModel by activityViewModel()
    var formattedDate: String? = null
    var stageAdapter: StageAdapter? = null

    private val matchViewModel: MatchViewModel by viewModel()


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
        binding.seeAll.setOnClickListener {
            val allMatches = getAllMatches()
            val intent = Intent(requireContext(), AllMatchesActivity::class.java)
            intent.putParcelableArrayListExtra("MATCH_DATA", ArrayList(allMatches))
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            viewModel.loadMatchesWithStages(formattedDate)
        }

//        setupMatchSlider()

        setupNavigationButtons()

        // Load initial data
//        updateMatchesForSelectedDate()
    }

    private fun getAllMatches(): List<Match> {
        val allMatches = mutableListOf<Match>()
        matchesByDate.values.forEach { matches ->
            allMatches.addAll(matches)
        }
        return allMatches
    }


//    private var lastSelectedDate: String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    /*
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
                val formattedDate = LocalDate.parse(
                    selectedDate.fullDate,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ).format(DateTimeFormatter.ofPattern("yyyyMMdd"))

                // Only call API if the date has changed
    //            if (formattedDate != lastSelectedDate) {
    //                Log.d("TAG_DATE", "Selected Date: $formattedDate")
    //                lastSelectedDate = formattedDate

                    viewModel.loadMatches(formattedDate)

    //            } else {
    //                Log.d("TAG_DATE", "Same date clicked again → skipping API call")
    //            }
            }
            val layoutManagerr =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            binding.datesRecyclerView.apply {
                layoutManager = layoutManagerr
                adapter = dateAdapter
            }

            // Snap to center
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.datesRecyclerView)

            // Scroll listener for pagination
            binding.datesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val firstVisible = layoutManagerr.findFirstVisibleItemPosition()
                    val lastVisible = layoutManagerr.findLastVisibleItemPosition()

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
    */
    private fun setupDateRecyclerView() {
        // Generate initial list (today ± 7 days for example)
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

            val horizontalMargin =
                resources.getDimensionPixelOffset(R.dimen.viewpager_horizontal_margin)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.left = horizontalMargin / 2
                    outRect.right = horizontalMargin / 2
                }
            })
        }
    }

    /*
        private fun setupMatchList(matchesList : List<Match>) {
            matchListAdapter = MatchListAdapter(
                matchesList,
                onExpandClick = { match ->
                    // Handle expand/collapse
                    val currentMatches = matchesByDate[currentDate] ?: emptyList()
                    val updatedMatches = currentMatches.map {
                        if (it.id == match.id) {
                            it.copy(isExpanded = !it.isExpanded)
                        } else {
                            it
                        }
                    }
                    matchesByDate[currentDate] = updatedMatches
                    updateMatchListAdapter(updatedMatches)
                },
                onItemClick = { match ->
                    // Navigate to MatchDetailActivity
                    val intent = Intent(requireContext(), MatchDetailActivity::class.java)
                    intent.putExtra("MATCH_DATA", match as Parcelable)
                    startActivity(intent)
                }
            )

            binding.matchesRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = matchListAdapter
            }
        }
    */

    private fun updateMatchListAdapter(matches: List<Match>) {
        matchListAdapter = MatchListAdapter(
            matches,
            onExpandClick = { match ->
                // Handle expand/collapse
                val currentMatches = matchesByDate[currentDate] ?: emptyList()
                val updatedMatches = currentMatches.map {
                    if (it.id == match.id) {
                        it.copy(isExpanded = !it.isExpanded)
                    } else {
                        it
                    }
                }
                matchesByDate[currentDate] = updatedMatches
//                updateMatchListAdapter(updatedMatches)
            },
            onItemClick = { match ->
                // Navigate to MatchDetailActivity
                val intent = Intent(requireContext(), MatchDetailActivity::class.java)
                intent.putExtra("MATCH_DATA", match as Parcelable)
                startActivity(intent)
            }
        )
        binding.matchesRecyclerView.adapter = matchListAdapter
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


    /*
        private fun updateMatchesForSelectedDate() {
            val matches = matchesByDate[currentDate] ?: emptyList()

            // Update slider with click listener
            matchSliderAdapter = MatchSliderAdapter(matches) { match ->
                val intent = Intent(requireContext(), MatchDetailActivity::class.java)
                intent.putExtra("MATCH_DATA", match as Parcelable)
                startActivity(intent)
            }
            binding.matchSliderViewPager.adapter = matchSliderAdapter

            // Update list with both click listeners
    //        updateMatchListAdapter(matches)

            // Show/hide navigation buttons based on number of matches
            if (matches.size <= 1) {
                binding.btnPrevious.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
            } else {
                binding.btnPrevious.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }

            // Reset to first item
            binding.matchSliderViewPager.setCurrentItem(0, false)
        }
    */


    private fun setUpCompetitionRV() {
        binding.matchesRecyclerView.layoutManager = LinearLayoutManager((context as? MainActivity))
        stageAdapter = StageAdapter(mutableListOf<Stage>(), this@HomeFragment , this@HomeFragment)
        binding.matchesRecyclerView.adapter = stageAdapter


    }


    private fun observeCompetitions() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matchesFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            showLoading(false)
                            updateCompetitions(result.data)

//                            setLiveMAtchesList(result.data)

                            val todayFormatted =
                                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            Log.d("TAG_today", "observeCompetitions: $formattedDate")
                            if (formattedDate == todayFormatted) {
                                setLiveMAtchesList(result.data)
                                binding.matchesHeadingTv.setText(binding.root.context.getString(R.string.other_today_s_matches))

                            }
                            else{
                                binding.matchesHeadingTv.setText(binding.root.context.getString(R.string.other_matches))
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


    private fun updateCompetitions(data: List<Stage>) {
        stageAdapter?.updateData(data)
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
            binding.matchesRecyclerView.gone()
            binding.matchSliderViewPager.gone()
        }
    }

    private fun showError(throwable: Throwable) {
        Log.d(ApiResultTAG, "showError: ${throwable.message.toString()}")

        Toast.makeText(requireContext(), throwable.message ?: "Error", Toast.LENGTH_SHORT).show()
    }


    override fun onStageExpanded(stage: Stage) {
    }

//    fun setLiveMAtchesList(matchesList : List<Stage>){
//        val liveMatches = matchesList
//            .flatMap { it.matches }
//            .filter { match ->
//                val status = match.match_status
//                status.contains("\'")
//            }
//
//        binding.textView3.text = getString(
//            R.string.livematches
//        ) + " ${liveMatches.size}"
//
//
//
//        setupMatchSlider(liveMatches)
//
//    }

    /*
        fun setLiveMAtchesList(matchesList: List<Stage>) {
            val liveMatches = matchesList
                .flatMap { stage ->
                    stage.matches!!.map { match ->
                        if(!stage.badge_url.equals("null")) {
                            match.copy(tournamentLogo = stage.badge_url) // inject here
                        }
                        if(!stage.competition_name.equals("null")) {
                            match.copy(tournamentName = stage.stage_name) // inject here
                        }
                        else{
                            match
                        }
                    }
                }
                .filter { match ->
                    val status = match.match_status
                    status!!.contains("\'")
                }

            binding.textView3.text = getString(R.string.livematches) + " ${liveMatches.size}"
            setupMatchSlider(liveMatches)
        }
    */
    fun setLiveMAtchesList(matchesList: List<Stage>?) {
            val liveMatches = matchesList.orEmpty()
                .flatMap { stage ->
                    stage.matches.orEmpty().map { match ->
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
        matchViewModel.setMatch(matche)
        matche.match_id?.let { viewModel.loadMatchSummary(it) }
    }


}