package com.example.skillcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
                        binding.listOfEverythingRecycler.adapter = AdapterListOfAllPremiers(premier) {id -> openMoviePage(id)}
                    }
                    POPULAR_ALL_TIME -> {
                        binding.listOfEverythingRecycler.adapter = adapterPopular
                        viewModel.pagingDataPopular.collectLatest {
                            adapterPopular.submitData(it)
                        }
                    }
                    TOP_MOVIES -> {
                        binding.listOfEverythingRecycler.adapter = adapterPopular
                        viewModel.choosePopularType(TOP_MOVIES)
                        viewModel.pagingDataPopular.collectLatest {
                            adapterPopular.submitData(it)
                        }
                    }

                    FILTERED -> {
                        binding.listOfEverythingRecycler.adapter = adapterFiltered
                        viewModel.pagingDataFiltered.collectLatest {
                            adapterFiltered.submitData(it)
                        }
                    }

                    FRENCH_DRAMAS -> {
                        binding.listOfEverythingRecycler.adapter = adapterFiltered
                        viewModel.chooseFilteredGenre("драма")
                        viewModel.chooseFilteredCountry("Франция")
                        viewModel.pagingDataFiltered.collectLatest {
                            adapterFiltered.submitData(it)
                        }
                    }
                    TOP_TV -> {
                        binding.listOfEverythingRecycler.adapter = adapterPopular
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
            val action = ListOfEverythingFragmentDirections.actionListOfEverythingFragmentToFilmPage(it)
            findNavController().navigate(action)
        }

    }
}