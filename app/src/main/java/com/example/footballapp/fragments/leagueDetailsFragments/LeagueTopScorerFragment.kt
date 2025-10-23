package com.example.footballapp.fragments.leagueDetailsFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.leaguePlayerStats.Player
import com.example.footballapi.leaguePlayerStats.Scores
import com.example.footballapi.leaguePlayerStats.Stat
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.adapters.LeagueTopScorerAdapter
import com.example.footballapp.adapters.followingadapters.PlayersAdapter
import com.example.footballapp.databinding.FragmentLeagueTopScorerBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class LeagueTopScorerFragment : Fragment() {

    lateinit var binding : FragmentLeagueTopScorerBinding
    private var playersAdapter: LeagueTopScorerAdapter? = null
    private val viewModel: FootballViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeagueTopScorerBinding.inflate(layoutInflater,container,false)
        return binding.root
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlayerAdapter()
         observeLeagueTopScorer()


    }

    fun setPlayerAdapter() {
        playersAdapter = LeagueTopScorerAdapter()

        binding.rvPlayers.apply {
            adapter  = playersAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    private fun observeLeagueTopScorer() {
        this@LeagueTopScorerFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.leagueTopScorerFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoadingPlayerStats(true)
                        }

                        is ApiResult.Success -> {

                            val playerStats = result.data.stats
//                            val filteredPlayersList = getAllPlayers(playerStats)
                            val filteredPlayersList = mergePlayers(playerStats)
                            Log.d("TAG_leaguePlayers", "Top scorers: ${filteredPlayersList}")
                            if(filteredPlayersList?.size!=0) {
                                showLoadingPlayerStats(false)

                                filteredPlayersList?.let {
                                    playersAdapter?.submitList(it)
                                }
                            }
                            else{
                                showLoadingPlayerStats(null)

                            }
                        }

                        is ApiResult.Error -> {

                            showLoadingPlayerStats(null)
                        }
                    }
                }
            }
        }

//        (context as? LeagueDetailActivity)?.league?.let {
//            it.competition_id?.let { compID -> viewModel.loadLeaguePlayerStats(compID) }
//        }

        viewModel.loadLeaguePlayerStats((context as? LeagueDetailActivity)?.league?.competition_id?:"")

    }

    private fun showLoadingPlayerStats(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading standing or competitions: $show")

        show?.let {
            if (show) {
                binding.playerr1Shimmer.visible()
                binding.playerrnumberShimmer.visible()
                binding.rvPlayersShimmer.visible()

                binding.playerr1.invisible()
                binding.playerrnumber.invisible()
                binding.rvPlayers.invisible()


            } else {
                binding.playerr1Shimmer.invisible()
                binding.playerrnumberShimmer.invisible()
                binding.rvPlayersShimmer.invisible()

                binding.playerr1.visible()
                binding.playerrnumber.visible()
                binding.rvPlayers.visible()

            }
        } ?: run {
            Toast.makeText(binding.root.context, "No data available for top scorer", Toast.LENGTH_SHORT).show()
            binding.playerr1Shimmer.invisible()
            binding.playerrnumberShimmer.invisible()
            binding.rvPlayersShimmer.invisible()

            binding.playerr1.invisible()
            binding.playerrnumber.invisible()
            binding.rvPlayers.invisible()
         }
    }

   /* private fun getAllPlayers(stats: List<Stat>): List<Player> {
        // Flatten all players from all stat types into a single list
        return stats.flatMap { it.players }
    }*/

    private fun mergePlayers(stats: List<Stat>?): List<Player> {
        if (stats.isNullOrEmpty()) {
            Log.e("TAG_mergePlayers", "Stats list is null or empty")
            return emptyList()
        }

        val playerMap = mutableMapOf<String, Player>()
        val allPlayers = stats.flatMap { it.players ?: emptyList() }

        Log.d("TAG_mergePlayers", "Total players found: ${allPlayers.size}")

        allPlayers.forEach { player ->
            val playerName = player.player_name?.trim()?.lowercase()  // case-insensitive matching
            Log.d("TAG_mergePlayers", "Processing player: ${player.player_name}, key=$playerName")

            if (playerName.isNullOrEmpty()) {
                Log.w("TAG_mergePlayers", "Skipping player with null/empty name")
                return@forEach
            }

            val existing = playerMap[playerName]
            if (existing == null) {
                // First occurrence of this player → add directly
                playerMap[playerName] = player
            } else {
                // Merge scores safely
                val existingScores = existing.scores ?: Scores()
                val newScores = player.scores ?: Scores()

                val mergedScores = Scores(
                    `1` = newScores.`1` ?: existingScores.`1`,
                    `3` = newScores.`3` ?: existingScores.`3`,
                    `4` = newScores.`4` ?: existingScores.`4`,
                    `6` = newScores.`6` ?: existingScores.`6`,
                    `8` = newScores.`8` ?: existingScores.`8`
                )

                // Keep latest player info but merge scores
                playerMap[playerName] = existing.copy(scores = mergedScores)
            }
        }

        Log.d("TAG_mergePlayers", "Unique merged players: ${playerMap.size}")
        return playerMap.values.toList()
    }










}