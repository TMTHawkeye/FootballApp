package com.example.footballapp.repositories

import android.content.Context
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.models.Team

class MatchRepository (var context : Context){

    var matchSelected : Matche? = null
    var teamSelected : Team? = null
    var selectedNews : LatestNewsResponseItem? = null

}