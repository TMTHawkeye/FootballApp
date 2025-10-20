package com.example.footballapi.modelClasses.TeamTable

data class InitialData(
    val basicInfo: BasicInfo,
    val dropdownStages: List<DropdownStage>,
    val leagueTables: LeagueTables
)