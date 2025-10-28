package com.example.footballapp

import android.app.Dialog
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.example.footballapi.modelClasses.matchSummary.Events
import com.example.footballapi.sealedClasses.MatchEvent

object Helper {
    const val sharedPreferenceMode = 0
    const val sharedPreferenceName = "Authenticator"
    const val PREF_NAME_LANGUAGE = "LanguagePreferences"
    const val LANGUAGE_POSITION_KEY = "LANG_POS"
    var ApiResultTAG = "ApiResult_Tag"
    var MATCH_ID = "MATCH_ID"
    var LEAGUE_ID = "LEAGUE_ID"
    var TEAM_ID = "TEAM_ID"
    var TEAM_NAME = "TEAM_NAME"
    var imagePrefixCompetition = "https://storage.livescore.com/images/competition/high/"
    var imagePrefix = "https://storage.livescore.com/images/team/high/"

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }


    fun Events.toMatchEventList(): List<MatchEvent> {
        val allEvents = mutableListOf<MatchEvent>()

        goals?.forEach {
            allEvents.add(MatchEvent.GoalEvent(it.minute, it.player, it.team, it.score))
        }

        yellow_cards?.forEach {
            allEvents.add(MatchEvent.YellowCardEvent(it.minute, it.player, it.team))
        }

        red_cards?.forEach {
            allEvents.add(MatchEvent.RedCardEvent(it.minute, it.player, it.team))
        }

        substitutions?.forEach {
            allEvents.add(MatchEvent.SubstitutionEvent(it.minute, it.playerIn, it.playerOut, it.team))
        }

        // Sort events by minute (ascending)
        return allEvents.sortedBy { it.minute }
    }


      fun formatMatchStatus(status: String?): String {
        if (status.isNullOrBlank()) return ""

        return when {
            // Handle seconds (double apostrophe)
            status.endsWith("''") -> {
                status.removeSuffix("''") + " sec"
            }

            // Handle minutes (single apostrophe)
            status.endsWith("'") -> {
                status.removeSuffix("'") + " min"
            }

            // Keep normal statuses (HT, FT, NS, etc.)
            else -> status
        }
    }

    var noInternetDialog : Dialog?=null

//      fun setupNoInternetDialog(context : Context) {
//        noInternetDialog = Dialog(context)
//        noInternetDialog?.setContentView(R.layout.dialog_no_internet_con) // your layout
//        noInternetDialog?.setCancelable(false)
//
//        val tryAgainBtn = noInternetDialog?.findViewById<TextView>(R.id.tryAgainBtn)
//        tryAgainBtn?.setOnClickListener {
//            if (isNetworkAvailable(context)) {
//                noInternetDialog?.dismiss()
//            }
//        }
//    }

    fun setupNoInternetDialog(context: Context) {
        noInternetDialog = Dialog(context)
        noInternetDialog?.apply {
            setContentView(R.layout.dialog_no_internet_con)
            setCancelable(false)

            // 🟢 Make dialog match parent
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            // 🟢 Make dialog background transparent nnn       n nnnnnnmm
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // 🟢 Optional: remove default dialog dim
//            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            val tryAgainBtn = findViewById<TextView>(R.id.tryAgainBtn)
            tryAgainBtn?.setOnClickListener {
                if (isNetworkAvailable(context)) {
                    dismiss()
                }
            }
        }
    }

//    fun setupNoInternetDialog(context: Context) {
//        noInternetDialog = Dialog(context)
//        noInternetDialog?.apply {
//            setContentView(R.layout.dialog_no_internet_con)
//            setCancelable(false)
//
//            // 🟢 Make dialog match parent
//            window?.setLayout(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//
//            // 🟢 Make dialog background transparent
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            val tryAgainBtn = findViewById<TextView>(R.id.tryAgainBtn)
//            tryAgainBtn?.setOnClickListener {
//                dismiss()
//
//                // 🕑 Check again after 2 seconds
//                Handler(Looper.getMainLooper()).postDelayed({
//                    if (!isNetworkAvailable(context)) {
//                        // still not connected, show dialog again
//                        if (!(noInternetDialog?.isShowing ?: false)) {
//                            noInternetDialog?.show()
//                        }
//                    }
//                }, 2000)
//            }
//        }
//    }



    fun isNetworkAvailable(context : Context): Boolean {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }



}