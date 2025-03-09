package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private var typeFilter: String? = null
    private var countryFilter: String? = null
    private var genreFilter: String? = null
    private var sortFilter: String? = null
    private var ratingFromFilter: Float = 1.0f
    private var ratingToFilter: Float = 10.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SearchFilterFragmentArgs by navArgs()
        checkForFiltersUpdate(args)

        binding.chooseCountryChoice.text = countryFilter ?: "Россия"
        binding.chooseGenreChoice.text = genreFilter ?: "Боевик"

        binding.headerBackButton.setOnClickListener {
            val action =
                SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchPageFragment(
                    type = typeFilter,
                    sort = sortFilter,
                    country = countryFilter,
                    genre = genreFilter,
                    ratingFrom = ratingFromFilter,
                    ratingTo = ratingToFilter
                )
            findNavController().navigate(action)
        }

        binding.selectWatchTypeGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.select_all -> typeFilter = "ALL"
                R.id.select_films -> typeFilter = "FILM"
                R.id.select_TVseries -> typeFilter = "TV_SERIES"
            }
        }

        binding.selectFilmSortGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.select_date -> sortFilter = "YEAR"
                R.id.select_popularity -> sortFilter = "NUM_VOTE"
                R.id.select_ratings -> sortFilter = "RATING"
            }
        }

        binding.chooseCountry.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterCountryFragment(genreFilter))
        }

        binding.chooseGenre.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterGenreFragment(countryFilter))
        }

        binding.chooseYear.setOnClickListener {
            findNavController().navigate(SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchFilterYearFragment())
        }


        binding.ratingSelectSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            ratingFromFilter = values[0]
            ratingToFilter = values[1]
        }

    }

    private fun checkForFiltersUpdate(arguments: SearchFilterFragmentArgs) {
        val country = arguments.CountryFilter
        country?.let {
            countryFilter = it
        }
        val genre = arguments.GenreFilter

        genre?.let {
            genreFilter = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}