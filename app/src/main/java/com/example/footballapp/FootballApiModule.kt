package com.example.footballapp



import com.example.footballapi.FootballApiClient
import com.example.footballapi.FootballRepository
import com.example.footballapi.FootballViewModel
import com.example.footballapp.repositories.FollowRepository
import com.example.footballapp.repositories.FollowTeamRepository
import com.example.footballapp.viewmodels.FollowTeamViewModel
import com.example.footballapp.viewmodels.FollowViewModel
import com.example.footballapp.viewmodels.SearchSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val footballApiModule = module {
    single {
        FootballApiClient(
            baseUrl = "https://footballdata.fastdl.video/",
            apiKey = "Qr6FggaW3U5kvPPSlpE52fZD0zIVNvYV0cmWpKpNh6K2IL0mXR"
        ).createService()
    }
    single { FootballRepository(get()) }
    viewModel { FootballViewModel(get()) }

    single { FollowRepository(get()) }
    viewModel { FollowViewModel(get()) }


    single { FollowTeamRepository(get()) }
    viewModel { FollowTeamViewModel(get()) }

    viewModel { SearchSharedViewModel() }
}
