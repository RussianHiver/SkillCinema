package com.example.skillcinema.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.entity.FilmEntity

class DataViewModel: ViewModel() {

    private val _countryFilter = MutableLiveData("Россия")
    val countryFilter: LiveData<String> get() = _countryFilter

    private val _genreFilter = MutableLiveData("Боевик")
    val genreFilter: LiveData<String> get() = _genreFilter

    private val _typeFilter = MutableLiveData("ALL")
    val typeFilter: LiveData<String> get() = _typeFilter

    private val _orderFilter = MutableLiveData("YEAR")
    val orderFilter: LiveData<String> get() = _orderFilter

    private val _yearFrom = MutableLiveData(1998)
    val yearFrom: LiveData<Int> get() = _yearFrom

    private val _yearTo = MutableLiveData(2024)
    val yearTo: LiveData<Int> get() = _yearTo

    private val _ratingFromFilter = MutableLiveData(0.0f)
    val ratingFromFilter: LiveData<Float> get() = _ratingFromFilter

    private val _ratingToFilter = MutableLiveData(10.0f)
    val ratingToFilter: LiveData<Float> get() = _ratingToFilter

    private val _filmography = MutableLiveData<List<StaffMemberDTO.Film>>()
    val filmography: LiveData<List<StaffMemberDTO.Film>> get() = _filmography

    private val _watchedFilmsList = MutableLiveData<List<FilmEntity>>(emptyList())
    val watchedFilmsList: LiveData<List<FilmEntity>> get() = _watchedFilmsList

    private val _interestingFilmsList = MutableLiveData<List<FilmEntity>>(emptyList())
    val interestingFilmsList: LiveData<List<FilmEntity>> get() = _interestingFilmsList

    private val _movieData = MutableLiveData<MovieDataDTO>()
    val movieData: LiveData<MovieDataDTO> get() = _movieData


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

    fun setRatingFromFilter(filter: Float) {
        _ratingFromFilter.value = filter
    }

    fun setRatingToFilter(filter: Float) {
        _ratingToFilter.value = filter
    }

    fun setYearFromFilter(filter: Int) {
        _yearFrom.value = filter
    }

    fun setYearToFilter(filter: Int) {
        _yearTo.value = filter
    }

    fun setFilmography(list: List<StaffMemberDTO.Film>) {
        _filmography.value = list
    }

    fun setWatchedFilmsList(list: List<FilmEntity>) {
        _watchedFilmsList.value = list
    }

    fun setInterestingFilmsList(list: List<FilmEntity>) {
        _interestingFilmsList.value = list
    }

    fun setMovieData(movie: MovieDataDTO) {
        _movieData.value = movie
    }

}