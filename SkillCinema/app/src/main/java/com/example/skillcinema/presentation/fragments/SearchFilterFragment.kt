package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.databinding.FragmentSearchFilterBinding


class SearchFilterFragment : Fragment() {

    var _binding: FragmentSearchFilterBinding? = null
    val binding: FragmentSearchFilterBinding get() = _binding!!

    private var countryFilter: String? = null
    private var genreFilter: String? = null

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
        binding.chooseGenreChoice.text = genreFilter ?: "Комедия"

        binding.headerBackButton.setOnClickListener {
            val action =
                SearchFilterFragmentDirections.actionSearchFilterFragmentToSearchPageFragment()
            findNavController().navigate(action)
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