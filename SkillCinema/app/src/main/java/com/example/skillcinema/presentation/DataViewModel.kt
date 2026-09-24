package com.example.skillcinema.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.data.constants.filmfilters.FilmCountryFilters
import com.example.skillcinema.data.constants.filmfilters.FilmGenreFilter
import com.example.skillcinema.data.constants.filmfilters.FilmOrderFilter
import com.example.skillcinema.data.constants.filmfilters.FilmRatingFilter
import com.example.skillcinema.data.constants.filmfilters.FilmTypeFilter
import com.example.skillcinema.data.constants.filmfilters.FilmYearFilter
import com.example.skillcinema.entity.FilmEntity

class DataViewModel : ViewModel() {

    private val _countryFilter = MutableLiveData(FilmCountryFilters.RUSSIA.filter)
    val countryFilter: LiveData<String> get() = _countryFilter

    private val _genreFilter = MutableLiveData(FilmGenreFilter.ACTION.filter)
    val genreFilter: LiveData<String> get() = _genreFilter

    private val _typeFilter = MutableLiveData(FilmTypeFilter.ALL.filter)
    val typeFilter: LiveData<String> get() = _typeFilter

    private val _orderFilter = MutableLiveData(FilmOrderFilter.YEAR.filter)
    val orderFilter: LiveData<String> get() = _orderFilter

    private val _yearFrom = MutableLiveData(FilmYearFilter.YEAR_FROM.filter)
    val yearFrom: LiveData<Int> get() = _yearFrom

    private val _yearTo = MutableLiveData(FilmYearFilter.YEAR_TO.filter)
    val yearTo: LiveData<Int> get() = _yearTo

    private val _ratingFromFilter = MutableLiveData(FilmRatingFilter.RATING_FROM.filter)
    val ratingFromFilter: LiveData<Float> get() = _ratingFromFilter

    private val _ratingToFilter = MutableLiveData(FilmRatingFilter.RATING_TO.filter)
    val ratingToFilter: LiveData<Float> get() = _ratingToFilter

    private val _filmography = MutableLiveData<List<StaffMemberDTO.Film>>()
    val filmography: LiveData<List<StaffMemberDTO.Film>> get() = _filmography

    private val _watchedFilmsList = MutableLiveData<List<FilmEntity>>(emptyList())
    val watchedFilmsList: LiveData<List<FilmEntity>> get() = _watchedFilmsList

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

    fun setMovieData(movie: MovieDataDTO) {
        _movieData.value = movie
    }

}