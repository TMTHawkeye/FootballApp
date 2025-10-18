package com.example.footballapp.utils

import com.example.footballapp.repositories.MatchRepository
import com.example.footballapp.viewmodels.MatchViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appMi = module {

    single {
        MatchRepository(get())
    }
     viewModel { MatchViewModel(get()) }
     viewModel { TeamViewmodel(get()) }
}