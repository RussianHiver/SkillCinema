package com.example.skillcinema.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel: ViewModel() {

    private val _countryFilter = MutableLiveData("США")
    val countryFilter: LiveData<String> get() = _countryFilter

    private val _genreFilter = MutableLiveData("Боевик")
    val genreFilter: LiveData<String> get() = _genreFilter

    private val _typeFilter = MutableLiveData("ALL")
    val typeFilter: LiveData<String> get() = _typeFilter

    private val _orderFilter = MutableLiveData("RATING")
    val orderFilter: LiveData<String> get() = _orderFilter

    fun setCountryFilter(filter: String) {
        _countryFilter.value = filter
    }

    fun setGenreFilter(filter: String) {
        _genreFilter.value = filter
    }

    fun setTypeFilter(filter: String) {
        _typeFilter.value = filter
    }

    fun setOrderFilter(filter: String) {
        _orderFilter.value = filter
    }
}