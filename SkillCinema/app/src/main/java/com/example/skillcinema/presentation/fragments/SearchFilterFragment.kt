package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.App
import com.example.skillcinema.data.constants.filmfilters.FilmCountryFilters
import com.example.skillcinema.data.constants.filmfilters.FilmGenreFilter
import com.example.skillcinema.data.constants.filmfilters.FilmOrderFilter
import com.example.skillcinema.data.constants.filmfilters.FilmRatingFilter
import com.example.skillcinema.data.constants.filmfilters.FilmTypeFilter
import com.example.skillcinema.data.constants.filmfilters.FilmYearFilter
import com.example.skillcinema.databinding.FragmentSearchFilterBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_COUNTRY
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_GENRE
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_ORDER
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_RATING_FROM
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_RATING_TO
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_TYPE
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_YEAR_FROM
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_YEAR_TO
import com.example.skillcinema.presentation.SharedPreferencesKeys.SHARED


class SearchFilterFragment : Fragment() {

    var _binding: FragmentSearchFilterBinding? = null
    val binding: FragmentSearchFilterBinding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var yearFromFilter = 1998
        var yearToFilter = 2024

        checkForLastFilters()

        with(dataViewModel) {
            countryFilter.observe(viewLifecycleOwner) {
                binding.chooseCountryChoice.text = it
            }

            genreFilter.observe(viewLifecycleOwner) {
                binding.chooseGenreChoice.text = it
            }

            yearFrom.observe(viewLifecycleOwner) {
                yearFromFilter = it
                binding.chooseYearChoice.text = "с $yearFromFilter по $yearToFilter"
            }

            yearTo.observe(viewLifecycleOwner) {
                yearToFilter = it
                binding.chooseYearChoice.text = "с $yearFromFilter по $yearToFilter"
            }
        }

        binding.selectWatchTypeGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.select_all -> {
                    dataViewModel.setTypeFilter(FilmTypeFilter.ALL.filter)
                    editor.apply {
                        putString(FILTER_FILM_TYPE, FilmTypeFilter.ALL.filter)
                    }.apply()
                }

                R.id.select_films -> {
                    dataViewModel.setTypeFilter(FilmTypeFilter.FILM.filter)
                    editor.apply {
                        putString(FILTER_FILM_TYPE, FilmTypeFilter.FILM.filter)
                    }.apply()
                }

                R.id.select_TVseries -> {
                    dataViewModel.setTypeFilter(FilmTypeFilter.TV_SERIES.filter)
                    editor.apply {
                        putString(FILTER_FILM_TYPE, FilmTypeFilter.TV_SERIES.filter)
                    }.apply()
                }
            }
        }

        binding.selectFilmSortGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.select_date -> {
                    dataViewModel.setOrderFilter(FilmOrderFilter.YEAR.filter)
                    editor.apply {
                        putString(FILTER_FILM_ORDER, FilmOrderFilter.YEAR.filter)
                    }.apply()
                }

                R.id.select_popularity -> {
                    dataViewModel.setOrderFilter(FilmOrderFilter.NUM_VOTE.filter)
                    editor.apply {
                        putString(FILTER_FILM_ORDER, FilmOrderFilter.NUM_VOTE.filter)
                    }.apply()
                }

                R.id.select_ratings -> {
                    dataViewModel.setOrderFilter(FilmOrderFilter.RATING.filter)
                    editor.apply {
                        putString(FILTER_FILM_ORDER, FilmOrderFilter.RATING.filter)
                    }.apply()
                }
            }
        }

        binding.chooseCountry.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterCountryFragment())
        }

        binding.chooseGenre.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterGenreFragment())
        }

        binding.chooseYear.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterYearFragment())
        }


        binding.ratingSelectSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            dataViewModel.setRatingFromFilter(values[0])
            dataViewModel.setRatingToFilter(values[1])
            editor.apply {
                putFloat(FILTER_FILM_RATING_FROM, values[0])
                putFloat(FILTER_FILM_RATING_TO, values[1])
            }.apply()
        }

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkForLastFilters() {
        val filmType = sharedPreferences.getString(FILTER_FILM_TYPE, FilmTypeFilter.ALL.filter)
        val filmOrder = sharedPreferences.getString(FILTER_FILM_ORDER, FilmOrderFilter.YEAR.filter)
        val filmCountry =
            sharedPreferences.getString(FILTER_FILM_COUNTRY, FilmCountryFilters.RUSSIA.filter)
        val filmGenre =
            sharedPreferences.getString(FILTER_FILM_GENRE, FilmGenreFilter.ACTION.filter)
        val filmRatingFrom =
            sharedPreferences.getFloat(FILTER_FILM_RATING_FROM, FilmRatingFilter.RATING_FROM.filter)
        val filmRatingTo =
            sharedPreferences.getFloat(FILTER_FILM_RATING_TO, FilmRatingFilter.RATING_TO.filter)
        val filmYearFrom =
            sharedPreferences.getInt(FILTER_YEAR_FROM, FilmYearFilter.YEAR_FROM.filter)
        val filmYearTo = sharedPreferences.getInt(FILTER_YEAR_TO, FilmYearFilter.YEAR_TO.filter)

        dataViewModel.setCountryFilter(filmCountry ?: FilmCountryFilters.RUSSIA.filter)
        dataViewModel.setGenreFilter(filmGenre ?: FilmGenreFilter.ACTION.filter)
        dataViewModel.setYearFromFilter(filmYearFrom)
        dataViewModel.setYearToFilter(filmYearTo)

        with(binding) {
            when (filmType) {
                FilmTypeFilter.FILM.filter -> selectWatchTypeGroup.check(R.id.select_films)
                FilmTypeFilter.TV_SERIES.filter -> selectWatchTypeGroup.check(R.id.select_TVseries)
                else -> selectWatchTypeGroup.check(R.id.select_all)
            }
            when (filmOrder) {
                FilmOrderFilter.NUM_VOTE.filter -> selectFilmSortGroup.check(R.id.select_popularity)
                FilmOrderFilter.RATING.filter -> selectFilmSortGroup.check(R.id.select_ratings)
                else -> selectFilmSortGroup.check(R.id.select_date)
            }
            ratingSelectSlider.setValues(filmRatingFrom, filmRatingTo)
        }
    }

}