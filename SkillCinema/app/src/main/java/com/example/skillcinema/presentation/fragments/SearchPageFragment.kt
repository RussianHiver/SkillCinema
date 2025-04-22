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
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSearchPageBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForSearchResult
import com.example.skillcinema.presentation.adapters.MovieDataLoadStateAdapter
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
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    val adapter = AdapterForSearchResult { id -> openMoviePage(id) }

    private var searchCountryFilter = ""
    private var searchGenreFilter = ""
    private var searchTypeFilter = ""
    private var searchOrderFilter = ""
    private var searchRatingFromFilter = 0.0f
    private var searchRatingToFilter = 10.0f
    private var searchYearFromFilter = 1998
    private var searchYearToFilter = 2024

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
                BottomSheetErrorFragment().show(parentFragmentManager, "sheetError")
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
        dataViewModel.setOrderFilter(filmOrder ?: "YEAR")
        dataViewModel.setTypeFilter(filmType ?: "ALL")
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