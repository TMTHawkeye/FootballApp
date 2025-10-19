package com.example.footballapp.fragments.matchdetail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.matchLineups.AwayStarter
import com.example.footballapi.modelClasses.matchLineups.HomeStarter
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.R
import com.example.footballapp.activities.MatchDetailActivity


import com.example.footballapp.databinding.FragmentLineupBinding
import com.example.footballapp.databinding.LineupFragmentNewBinding
import com.example.footballapp.models.matchmodels.Match
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class LineupFragment : Fragment() {

    private lateinit var binding: LineupFragmentNewBinding

    //    private lateinit var match: Match
    private val viewModel: FootballViewModel by activityViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        match = arguments?.getSerializable("MATCH") as Match
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LineupFragmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMatchStats()

    }

    private fun observeMatchStats() {
        this@LineupFragment.lifecycleScope.launch {
            viewModel.matchLineupFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)
                        val stats = result.data
                        Log.d(
                            "MATCH_LINEUP",
                            "lineup: ${stats.event.lineups.homeStarters} and  ${stats.event.lineups.awayStarters}"
                        )
                        displayLineups(
                            stats.event.lineups.homeStarters,
                            stats.event.lineups.awayStarters
                        )
                    }

                    is ApiResult.Error -> {
                        showLoading(false)
//                        showError(result.throwable)
                    }
                }
            }
        }

//        viewModel.loadMatchLineups("1426226")

        (context as? MatchDetailActivity)?.match?.match_id?.let {
            viewModel.loadMatchLineups(it)
        }
    }

    private fun showLoading(show: Boolean) {
        Log.d(ApiResultTAG, "showLoading: $show")

        if (show) {
//            binding.ctShimmers.visible()
//            binding.ctSliderShimmer.visible()
        } else {
//            binding.ctShimmers.gone()
//            binding.ctSliderShimmer.gone()
        }
    }



    fun displayLineups(
        homeLineup: List<HomeStarter>,
        awayLineup: List<AwayStarter>
    ) {
        // Clear old views
        binding.homeGoalkeeperRow.removeAllViews()
        binding.homeDefendersRow.removeAllViews()
        binding.homeMidfieldersRow.removeAllViews()
        binding.homeForwardsRow.removeAllViews()

        binding.awayGoalkeeperRow.removeAllViews()
        binding.awayDefendersRow.removeAllViews()
        binding.awayMidfieldersRow.removeAllViews()
        binding.awayForwardsRow.removeAllViews()

        val inflater = LayoutInflater.from(context)

        fun createPlayerView(
            isHomePlayer : Boolean = true,
            playerName: String,
            playerNumber: String,
            goal: Any?,
            card: String?
        ): View {
            val view = inflater.inflate(R.layout.item_player_formation, null, false)
            view.findViewById<TextView>(R.id.playerName).text = playerName
            view.findViewById<TextView>(R.id.playerNumber).text = "#$playerNumber"

            val icon = view.findViewById<ImageView>(R.id.playerIconImg)
            when {
                card == null -> {
//                    icon.setColorFilter(Color.YELLOW)
                    if(isHomePlayer) {
                        val greenColor = ContextCompat.getColor(icon.context, R.color.green_color)
                        icon.setColorFilter(greenColor)
                    }
                    else{
                        val greenColor = ContextCompat.getColor(icon.context, R.color.redPlayer_color)
                        icon.setColorFilter(greenColor)
                    }
                }
                card == "FootballYellowCard" -> icon.setColorFilter(Color.YELLOW)
                card == "FootballRedCard" -> icon.setColorFilter(Color.RED)

            }
            return view
        }

        fun addPlayersToRow(row: LinearLayout, count: Int, players: List<View>) {
            row.removeAllViews()

            // Wait for layout to measure width
            row.post {
                val totalWidth =
                    row.width.takeIf { it > 0 } ?: row.resources.displayMetrics.widthPixels
                val maxPerRow = count

                val spacing =
                    row.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp)
                val playerWidth = (totalWidth - (spacing * (maxPerRow + 1))) / maxPerRow

                players.forEach { playerView ->
                    val params = LinearLayout.LayoutParams(
                        playerWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.marginStart = spacing / 2
                    params.marginEnd = spacing / 2
                    playerView.layoutParams = params

                    val icon = playerView.findViewById<ImageView>(R.id.playerIconImg)
                    val name = playerView.findViewById<TextView>(R.id.playerName)
                    val number = playerView.findViewById<TextView>(R.id.playerNumber)

                    when {
                        count > 6 -> { // many players → smaller
                            val small =
                                row.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._18sdp)
                            icon.layoutParams.width = small
                            icon.layoutParams.height = small
                            name.textSize = 7f
                            number.textSize = 7f
                        }

                        count > 5 -> { // 6 players
                            val medium =
                                row.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._22sdp)
                            icon.layoutParams.width = medium
                            icon.layoutParams.height = medium
                            name.textSize = 8f
                            number.textSize = 8f
                        }

                        else -> { // normal size
                            val normal =
                                row.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._27sdp)
                            icon.layoutParams.width = normal
                            icon.layoutParams.height = normal
                            name.textSize = 9f
                            number.textSize = 9f
                        }
                    }

                    row.addView(playerView)
                }
            }
        }

        // Group players by position for both teams
        val homeGroups = homeLineup.groupBy { it.fieldPosition.split(":")[0] }
        val awayGroups = awayLineup.groupBy { it.fieldPosition.split(":")[0] }

        // 🏠 HOME TEAM (Top)
        homeGroups["1"]?.let {
            addPlayersToRow(
                binding.homeGoalkeeperRow,
                it.size,
                it.map { p -> createPlayerView(true,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        homeGroups["2"]?.let {
            addPlayersToRow(
                binding.homeDefendersRow,
                it.size,
                it.map { p -> createPlayerView(true,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        homeGroups["3"]?.let {
            addPlayersToRow(
                binding.homeMidfieldersRow,
                it.size,
                it.map { p -> createPlayerView(true,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        homeGroups["4"]?.let {
            addPlayersToRow(
                binding.homeForwardsRow,
                it.size,
                it.map { p -> createPlayerView(true,p.shortName, p.number.toString(), p.goal, p.card) })
        }

        // 🛫 AWAY TEAM (Bottom)
        awayGroups["4"]?.let {
            addPlayersToRow(
                binding.awayForwardsRow,
                it.size,
                it.map { p -> createPlayerView(false,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        awayGroups["3"]?.let {
            addPlayersToRow(
                binding.awayMidfieldersRow,
                it.size,
                it.map { p -> createPlayerView(false,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        awayGroups["2"]?.let {
            addPlayersToRow(
                binding.awayDefendersRow,
                it.size,
                it.map { p -> createPlayerView(false,p.shortName, p.number.toString(), p.goal, p.card) })
        }
        awayGroups["1"]?.let {
            addPlayersToRow(
                binding.awayGoalkeeperRow,
                it.size,
                it.map { p -> createPlayerView(false,p.shortName, p.number.toString(), p.goal, p.card) })
        }
    }


}