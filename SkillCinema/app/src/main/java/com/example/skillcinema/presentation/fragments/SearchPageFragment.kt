package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.skillcinema.App
import com.example.skillcinema.data.constants.filmfilters.FilmCountryFilters
import com.example.skillcinema.data.constants.filmfilters.FilmGenreFilter
import com.example.skillcinema.data.constants.filmfilters.FilmOrderFilter
import com.example.skillcinema.data.constants.filmfilters.FilmRatingFilter
import com.example.skillcinema.data.constants.filmfilters.FilmTypeFilter
import com.example.skillcinema.data.constants.filmfilters.FilmYearFilter
import com.example.skillcinema.databinding.FragmentSearchPageBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_COUNTRY
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_GENRE
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_ORDER
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_RATING_FROM
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_RATING_TO
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_FILM_TYPE
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_YEAR_FROM
import com.example.skillcinema.presentation.SharedPreferencesKeys.FILTER_YEAR_TO
import com.example.skillcinema.presentation.SharedPreferencesKeys.SHARED
import com.example.skillcinema.presentation.adapters.AdapterForSearchResult
import com.example.skillcinema.presentation.adapters.MovieDataLoadStateAdapter
import com.example.skillcinema.presentation.fragments.BottomSheetKeys.SHEET_ERROR
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPageFragment : Fragment() {

    var _binding: FragmentSearchPageBinding? = null
    val binding: FragmentSearchPageBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

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

    val adapter = AdapterForSearchResult { id -> openMoviePage(id) }

    private var searchCountryFilter = ""
    private var searchGenreFilter = ""
    private var searchTypeFilter = ""
    private var searchOrderFilter = ""
    private var searchRatingFromFilter = FilmRatingFilter.RATING_FROM.filter
    private var searchRatingToFilter = FilmRatingFilter.RATING_TO.filter
    private var searchYearFromFilter = FilmYearFilter.YEAR_FROM.filter
    private var searchYearToFilter = FilmYearFilter.YEAR_TO.filter

    private var searchJob: Job? = null

    private var previousSearchResult = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForLastFilters()

        binding.searchTextInputLayout.setEndIconOnClickListener {
            val action =
                SearchPageFragmentDirections.actionSearchPageFragmentToSearchFilterFragment(
                    null,
                    null
                )
            findNavController().navigate(action)
        }

        with(dataViewModel) {
            countryFilter.observe(viewLifecycleOwner) { country ->
                searchCountryFilter = country
            }

            genreFilter.observe(viewLifecycleOwner) { genre ->
                var fixedGenre = genre
                fixedGenre?.let { string ->
                    fixedGenre = string.lowercase()
                }
                searchGenreFilter = fixedGenre
            }

            typeFilter.observe(viewLifecycleOwner) { type ->
                searchTypeFilter = type
            }

            orderFilter.observe(viewLifecycleOwner) { order ->
                searchOrderFilter = order
            }

            ratingFromFilter.observe(viewLifecycleOwner) { ratingFrom ->
                searchRatingFromFilter = ratingFrom
            }

            ratingToFilter.observe(viewLifecycleOwner) { ratingTo ->
                searchRatingToFilter = ratingTo
            }

            yearFrom.observe(viewLifecycleOwner) { yearFrom ->
                searchYearFromFilter = yearFrom
            }

            yearTo.observe(viewLifecycleOwner) { yearTo ->
                searchYearToFilter = yearTo
            }

        }

        binding.searchResultRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieDataLoadStateAdapter { adapter.retry() },
            footer = MovieDataLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.searchResultRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            handleError(loadState)
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                binding.noSearchResultText.isVisible = adapter.itemCount < 1
            } else {
                binding.noSearchResultText.isVisible = false
            }
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.pagingDataSearchResult.collectLatest {
                            it?.let {
                                adapter.submitData(it)
                            }
                        }
                    }
                }

                binding.searchTextInputEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        if (!search.isNullOrBlank() && search != previousSearchResult) {
                            searchJob?.cancel()
                            previousSearchResult = search.toString()
                            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.getNewPagerForSearchResult(
                                    search.toString(),
                                    country = searchCountryFilter,
                                    genre = searchGenreFilter,
                                    type = searchTypeFilter,
                                    order = searchOrderFilter,
                                    ratingFrom = searchRatingFromFilter,
                                    ratingTo = searchRatingToFilter,
                                    yearFrom = searchYearFromFilter,
                                    yearTo = searchYearToFilter
                                )
                            }
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }

                })
            } else {
                BottomSheetErrorFragment().show(parentFragmentManager, SHEET_ERROR)
            }
        }


    }

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action = SearchPageFragmentDirections.actionSearchPageFragmentToFilmPage(id)
            findNavController().navigate(action)
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val state = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        state?.let {
            Toast.makeText(activity, "${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.pagingDataSearchResult.value = null
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
        dataViewModel.setOrderFilter(filmOrder ?: FilmOrderFilter.YEAR.filter)
        dataViewModel.setTypeFilter(filmType ?: FilmTypeFilter.ALL.filter)
        dataViewModel.setRatingFromFilter(filmRatingFrom)
        dataViewModel.setRatingToFilter(filmRatingTo)
        dataViewModel.setYearFromFilter(filmYearFrom)
        dataViewModel.setYearToFilter(filmYearTo)
    }

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }

}