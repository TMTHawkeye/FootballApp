package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.repositories.MatchRepository

class MatchViewModel(var repo : MatchRepository) : ViewModel() {


    fun setMatch(match : Matche){
        repo.matchSelected = match
    }

    fun getMatch(): Matche? = repo.matchSelected
}