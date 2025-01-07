package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.databinding.FragmentHomepageBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForHomePage
import kotlinx.coroutines.launch

private const val PREMIERS = "premiers"
private const val POPULAR_ALL_TIME = "popular"
private const val TOP_MOVIES = "TOP_250_MOVIES"
private const val FILTERED = "filtered"
private const val FRENCH_DRAMAS = "french_dramas"
private const val TOP_TV = "TOP_250_TV_SHOWS"
private const val TOP_POPULAR = "TOP_POPULAR_ALL"

class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        DaggerAppComponent.create().mainViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val premier = viewModel.givePremierList()
            val premierAdapterForHomePage = AdapterForHomePage(
                PREMIERS,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            premierAdapterForHomePage.checkTheNumberOfItems(premier.size)
            premierAdapterForHomePage.addPremiers(premier)
            binding.recyclerPreview.adapter = premierAdapterForHomePage
            if (premier.size < 20) binding.allButtonPreview.isEnabled = false
            binding.allButtonPreview.setOnClickListener {
                openAllList(PREMIERS)
            }

            val popular = viewModel.givePopularList(1, TOP_POPULAR)
            val popularAdapterForHomePage = AdapterForHomePage(POPULAR_ALL_TIME,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            popularAdapterForHomePage.checkTheNumberOfItems(popular.size)
            popularAdapterForHomePage.addPopular(popular)
            binding.recyclerPopular.adapter = popularAdapterForHomePage
            if (premier.size < 20) binding.allButtonPopular.isEnabled = false
            binding.allButtonPopular.setOnClickListener {
                openAllList(POPULAR_ALL_TIME)
            }

            val usaAction = viewModel.giveFilteredList(1, "США", "боевик")
            val usaActionAdapterForHomePage = AdapterForHomePage(
                FILTERED,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            usaActionAdapterForHomePage.checkTheNumberOfItems(usaAction.size)
            usaActionAdapterForHomePage.addFiltered(usaAction)
            binding.recyclerUsaAction.adapter = usaActionAdapterForHomePage
            if (premier.size < 20) binding.allButtonUsaAction.isEnabled = false
            binding.allButtonUsaAction.setOnClickListener {
                openAllList(FILTERED)
            }

            val topMovies = viewModel.givePopularList(1, TOP_MOVIES)
            val topMoviesAdapterForHomePage = AdapterForHomePage(
                POPULAR_ALL_TIME,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            topMoviesAdapterForHomePage.checkTheNumberOfItems(topMovies.size)
            topMoviesAdapterForHomePage.addPopular(topMovies)
            binding.recyclerTop.adapter = topMoviesAdapterForHomePage
            if (premier.size < 20) binding.allButtonTop.isEnabled = false
            binding.allButtonTop.setOnClickListener {
                openAllList(TOP_MOVIES)
            }

            val frenchDramas = viewModel.giveFilteredList(1, "Франция", "драма")
            val frenchDramasAdapterForHomePage = AdapterForHomePage(
                FILTERED,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            frenchDramasAdapterForHomePage.checkTheNumberOfItems(frenchDramas.size)
            frenchDramasAdapterForHomePage.addFiltered(frenchDramas)
            binding.recyclerFrenchDramas.adapter = frenchDramasAdapterForHomePage
            if (premier.size < 20) binding.allButtonFrenchDramas.isEnabled = false
            binding.allButtonFrenchDramas.setOnClickListener {
                openAllList(FRENCH_DRAMAS)
            }

            val tvSeries = viewModel.givePopularList(1, TOP_TV)
            val tvSeriesAdapterForHomePage = AdapterForHomePage(
                POPULAR_ALL_TIME,
                { id -> openMoviePage(id) }) { list -> openAllList(list) }
            tvSeriesAdapterForHomePage.checkTheNumberOfItems(tvSeries.size)
            tvSeriesAdapterForHomePage.addPopular(tvSeries)
            binding.recyclerTVSeries.adapter = tvSeriesAdapterForHomePage
            if (premier.size < 20) binding.allButtonTVSeries.isEnabled = false
            binding.allButtonTVSeries.setOnClickListener {
                openAllList(TOP_TV)
            }
        }
    }

    fun openMoviePage(id: Int?) {
        id?.let {
            val action = HomepageFragmentDirections.actionHomepageFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

    fun openAllList(list: String) {
        val action =
            HomepageFragmentDirections.actionHomepageFragmentToListOfEverythingFragment(list)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
