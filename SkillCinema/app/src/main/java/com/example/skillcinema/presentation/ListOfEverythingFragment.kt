package com.example.skillcinema.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.skillcinema.data.AdapterListOfAllFiltered
import com.example.skillcinema.data.AdapterListOfAllPopular
import com.example.skillcinema.data.AdapterListOfAllPremiers
import com.example.skillcinema.data.MovieDataLoadStateAdapter
import com.example.skillcinema.data.RetrofitInstance
import com.example.skillcinema.databinding.FragmentListOfEverythingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val PREMIERS = "premiers"
private const val POPULAR_ALL_TIME = "popular"
private const val TOP_MOVIES = "TOP_250_MOVIES"
private const val FILTERED = "filtered"
private const val FRENCH_DRAMAS = "french_dramas"
private const val TOP_TV = "TOP_250_TV_SHOWS"

class ListOfEverythingFragment : Fragment() {

    private var _binding: FragmentListOfEverythingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

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

        val args: ListOfEverythingFragmentArgs by navArgs()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (args.listOfMovies) {
                    PREMIERS -> {
                        val premier = RetrofitInstance.searchDataApi.getMoviePremieres().items
                        binding.listOfEverythingRecycler.adapter =
                            AdapterListOfAllPremiers(premier) { id -> openMoviePage(id) }
                    }

                    POPULAR_ALL_TIME -> {
                        binding.listOfEverythingRecycler.adapter =
                            adapterPopular.withLoadStateHeaderAndFooter(
                                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
                            )
                            adapterPopular.addLoadStateListener {loadState ->
                                binding.listOfEverythingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                                binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
                                binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                                handleError(loadState)
                            }
                        viewModel.pagingDataPopular.collectLatest {
                            adapterPopular.submitData(it)
                        }
                    }

                    TOP_MOVIES -> {
                        binding.listOfEverythingRecycler.adapter =
                            adapterPopular.withLoadStateHeaderAndFooter(
                                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
                            )
                        adapterPopular.addLoadStateListener {loadState ->
                            binding.listOfEverythingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
                            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                            handleError(loadState)
                        }
                        viewModel.choosePopularType(TOP_MOVIES)
                        viewModel.pagingDataPopular.collectLatest {
                            adapterPopular.submitData(it)
                        }
                    }

                    FILTERED -> {
                        binding.listOfEverythingRecycler.adapter =
                            adapterFiltered.withLoadStateHeaderAndFooter(
                                header = MovieDataLoadStateAdapter { adapterFiltered.retry() },
                                footer = MovieDataLoadStateAdapter { adapterFiltered.retry() }
                            )
                        adapterFiltered.addLoadStateListener {loadState ->
                            binding.listOfEverythingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
                            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                            handleError(loadState)
                        }
                        viewModel.pagingDataFiltered.collectLatest {
                            adapterFiltered.submitData(it)
                        }
                    }

                    FRENCH_DRAMAS -> {
                        binding.listOfEverythingRecycler.adapter =
                            adapterFiltered.withLoadStateHeaderAndFooter(
                                header = MovieDataLoadStateAdapter { adapterFiltered.retry() },
                                footer = MovieDataLoadStateAdapter { adapterFiltered.retry() }
                            )
                        adapterFiltered.addLoadStateListener {loadState ->
                            binding.listOfEverythingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
                            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                            handleError(loadState)
                        }
                        viewModel.chooseFilteredGenre("драма")
                        viewModel.chooseFilteredCountry("Франция")
                        viewModel.pagingDataFiltered.collectLatest {
                            adapterFiltered.submitData(it)
                        }
                    }

                    TOP_TV -> {
                        binding.listOfEverythingRecycler.adapter =
                            adapterPopular.withLoadStateHeaderAndFooter(
                                header = MovieDataLoadStateAdapter { adapterPopular.retry() },
                                footer = MovieDataLoadStateAdapter { adapterPopular.retry() }
                            )
                        adapterPopular.addLoadStateListener {loadState ->
                            binding.listOfEverythingRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
                            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                            handleError(loadState)
                        }
                        viewModel.choosePopularType(TOP_TV)
                        viewModel.pagingDataPopular.collectLatest {
                            adapterPopular.submitData(it)
                        }
                    }
                }
            }
        }
    }

    fun openMoviePage(id: Int?) {
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
            Toast.makeText(activity, "${it.error}", Toast.LENGTH_SHORT).show()
        }
    }
}