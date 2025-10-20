package com.example.footballapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchSharedViewModel  : ViewModel(){

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}