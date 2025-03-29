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
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSearchFilterBinding
import com.example.skillcinema.presentation.DataViewModel


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
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
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
                    dataViewModel.setTypeFilter("ALL")
                    editor.apply {
                        putString("filterFilmType", "ALL")
                    }.apply()
                }
                R.id.select_films -> {
                    dataViewModel.setTypeFilter("FILM")
                    editor.apply {
                        putString("filterFilmType", "FILM")
                    }.apply()
                }
                R.id.select_TVseries -> {
                    dataViewModel.setTypeFilter("TV_SERIES")
                    editor.apply {
                        putString("filterFilmType", "TV_SERIES")
                    }.apply()
                }
            }
        }

        binding.selectFilmSortGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.select_date -> {
                    dataViewModel.setOrderFilter("YEAR")
                    editor.apply {
                        putString("filterFilmOrder", "YEAR")
                    }.apply()
                }
                R.id.select_popularity -> {
                    dataViewModel.setOrderFilter("NUM_VOTE")
                    editor.apply {
                        putString("filterFilmOrder", "NUM_VOTE")
                    }.apply()
                }
                R.id.select_ratings -> {
                    dataViewModel.setOrderFilter("RATING")
                    editor.apply {
                        putString("filterFilmOrder", "RATING")
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
            editor.apply{
                putFloat("filterFilmRatingFrom", values[0])
                putFloat("filterFilmRatingTo", values[1])
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
        val filmType = sharedPreferences.getString("filterFilmType", "ALL")
        val filmOrder = sharedPreferences.getString("filterFilmOrder", "YEAR")
        val filmCountry = sharedPreferences.getString("filterFilmCountry", "Россия")
        val filmGenre = sharedPreferences.getString("filterFilmGenre", "Боевик")
        val filmRatingFrom = sharedPreferences.getFloat("filterFilmRatingFrom", 0.0f)
        val filmRatingTo = sharedPreferences.getFloat("filterFilmRatingTo", 10.0f)
        val filmYearFrom = sharedPreferences.getInt("filterYearFrom", 1998)
        val filmYearTo = sharedPreferences.getInt("filterYearTo", 2024)

        dataViewModel.setCountryFilter(filmCountry ?: "Россия")
        dataViewModel.setGenreFilter(filmGenre ?: "Боевик")
        dataViewModel.setYearFromFilter(filmYearFrom)
        dataViewModel.setYearToFilter(filmYearTo)

        with(binding) {
            when(filmType) {
                "FILM" -> selectWatchTypeGroup.check(R.id.select_films)
                "TV_SERIES" -> selectWatchTypeGroup.check(R.id.select_TVseries)
                else -> selectWatchTypeGroup.check(R.id.select_all)
            }
            when(filmOrder) {
                "NUM_VOTE" -> selectFilmSortGroup.check(R.id.select_popularity)
                "RATING" -> selectFilmSortGroup.check(R.id.select_ratings)
                else -> selectFilmSortGroup.check(R.id.select_date)
            }
            ratingSelectSlider.setValues(filmRatingFrom, filmRatingTo)
        }
    }

}