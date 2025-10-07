package com.example.footballapp.interfaces

import com.example.footballapi.modelClasses.Matche

interface OnMatchSelected {

    fun onMatchClicked(matche: Matche)
}