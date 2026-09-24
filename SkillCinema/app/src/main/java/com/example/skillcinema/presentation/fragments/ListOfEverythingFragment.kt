package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.data.constants.FilmMainListTypes
import com.example.skillcinema.data.constants.filmfilters.FilmCountryFilters
import com.example.skillcinema.databinding.FragmentListOfEverythingBinding
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterListOfAllFiltered
import com.example.skillcinema.presentation.adapters.AdapterListOfAllPopular
import com.example.skillcinema.presentation.adapters.AdapterListOfAllPremiers
import com.example.skillcinema.presentation.adapters.MovieDataLoadStateAdapter
import com.example.skillcinema.presentation.fragments.BottomSheetKeys.SHEET_ERROR
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListOfEverythingFragment : Fragment() {

    private var _binding: FragmentListOfEverythingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private val adapterPopular by lazy { AdapterListOfAllPopular { id -> openMoviePage(id) } }

    private val adapterFiltered by lazy { AdapterListOfAllFiltered { id -> openMoviePage(id) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfEverythingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            val action =
                ListOfEverythingFragmentDirections.actionListOfEverythingFragmentToHomepageFragment()
            findNavController().navigate(action)
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                val args: ListOfEverythingFragmentArgs by navArgs()

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                        when (args.listOfMovies) {
                            FilmMainListTypes.PREMIERS.type -> {
                                fillPremierList()
                            }

                            FilmMainListTypes.POPULAR_ALL_TIME.type -> {
                                fillPopularList()
                            }

                            FilmMainListTypes.FILTERED.type -> {
                                fillUsaActionList()
                            }

                            FilmMainListTypes.TOP_MOVIES.type -> {
                                fillTopMovies()
                            }

                            FilmMainListTypes.FRENCH_DRAMAS.type -> {
                                fillFrenchDramas()
                            }

                            FilmMainListTypes.TOP_TV.type -> {
                                fillTopTV()
                            }
                        }

                    }
                }
            } else {
                BottomSheetErrorFragment().show(
                    parentFragmentManager,
                    SHEET_ERROR
                )
            }
        }


    }

    private suspend fun fillPremierList() {
        binding.stateProgress.isVisible = true
        val premier = viewModel.givePremierList()
        binding.headerTitle.text = getText(R.string.preview)
        binding.listOfEverythingRecycler.adapter =
            AdapterListOfAllPremiers(premier) { id -> openMoviePage(id) }
        binding.stateProgress.isVisible = false
    }

    private suspend fun fillPopularList() {
        binding.headerTitle.text = getText(R.string.popular)
        binding.listOfEverythingRecycler.adapter =
            adapterPopular.withLoadStateHeaderAndFooter(
                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
            )
        adapterPopular.addLoadStateListener { loadState ->
            binding.listOfEverythingRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible =
                loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible =
                loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }
        viewModel.choosePopularType(FilmMainListTypes.POPULAR_ALL_TIME.type)
        viewModel.pagingDataPopular.collectLatest {
            adapterPopular.submitData(it)
        }
    }

    private suspend fun fillUsaActionList() {
        binding.headerTitle.text = getText(R.string.usaAction)
        binding.listOfEverythingRecycler.adapter =
            adapterFiltered.withLoadStateHeaderAndFooter(
                header = MovieDataLoadStateAdapter { adapterFiltered.retry() },
                footer = MovieDataLoadStateAdapter { adapterFiltered.retry() }
            )
        adapterFiltered.addLoadStateListener { loadState ->
            binding.listOfEverythingRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible =
                loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible =
                loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }
        viewModel.putCountryAndGenre(
            genre = getString(R.string.genre_action),
            country = FilmCountryFilters.USA.filter
        )
        viewModel.pagingDataFiltered.collectLatest {
            adapterFiltered.submitData(it)
        }
    }

    private suspend fun fillTopMovies() {
        binding.headerTitle.text = getText(R.string.topMovies)
        binding.listOfEverythingRecycler.adapter =
            adapterPopular.withLoadStateHeaderAndFooter(
                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
            )
        adapterPopular.addLoadStateListener { loadState ->
            binding.listOfEverythingRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible =
                loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible =
                loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }
        viewModel.choosePopularType(FilmMainListTypes.TOP_MOVIES.type)
        viewModel.pagingDataPopular.collectLatest {
            adapterPopular.submitData(it)
        }
    }

    private suspend fun fillFrenchDramas() {
        binding.headerTitle.text = getText(R.string.frenchDramas)
        binding.listOfEverythingRecycler.adapter =
            adapterFiltered.withLoadStateHeaderAndFooter(
                header = MovieDataLoadStateAdapter { adapterFiltered.retry() },
                footer = MovieDataLoadStateAdapter { adapterFiltered.retry() }
            )
        adapterFiltered.addLoadStateListener { loadState ->
            binding.listOfEverythingRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible =
                loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible =
                loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }
        viewModel.putCountryAndGenre(
            genre = getString(R.string.genre_drama),
            country = FilmCountryFilters.FRANCE.filter
        )
        viewModel.pagingDataFiltered.collectLatest {
            adapterFiltered.submitData(it)
        }
    }

    private suspend fun fillTopTV() {
        binding.headerTitle.text = getText(R.string.tvSeries)
        binding.listOfEverythingRecycler.adapter =
            adapterPopular.withLoadStateHeaderAndFooter(
                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
            )
        adapterPopular.addLoadStateListener { loadState ->
            binding.listOfEverythingRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible =
                loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible =
                loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }
        viewModel.choosePopularType(FilmMainListTypes.TOP_TV.type)
        viewModel.pagingDataPopular.collectLatest {
            adapterPopular.submitData(it)
        }
    }

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action =
                ListOfEverythingFragmentDirections.actionListOfEverythingFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(activity, it.error.localizedMessage, Toast.LENGTH_SHORT).show()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}