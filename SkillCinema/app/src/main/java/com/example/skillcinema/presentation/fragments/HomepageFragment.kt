package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.data.constants.FilmMainListTypes
import com.example.skillcinema.data.constants.filmfilters.FilmCountryFilters
import com.example.skillcinema.databinding.FragmentHomepageBinding
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForHomePage
import com.example.skillcinema.presentation.fragments.BottomSheetKeys.SHEET_ERROR
import kotlinx.coroutines.launch

class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
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

        val favoriteFilm = CollectionEntity(1, getString(R.string.collection_favourites))
        val bookmarkFilm = CollectionEntity(2, getString(R.string.collection_want_to_watch))
        val visibilityFilm = CollectionEntity(3, getString(R.string.collection_watched_movies))

        context?.let {
            if (checkForInternet(it)) {

                viewModel.insertCollection(favoriteFilm)
                viewModel.insertCollection(bookmarkFilm)
                viewModel.insertCollection(visibilityFilm)

                viewLifecycleOwner.lifecycleScope.launch {
                    val premier = viewModel.givePremierList()
                    val premierAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.PREMIERS.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    premierAdapterForHomePage.checkTheNumberOfItems(premier.size)
                    premierAdapterForHomePage.addPremiers(premier)
                    binding.recyclerPreview.adapter = premierAdapterForHomePage
                    if (premier.size < 20) binding.allButtonPreview.isEnabled = false
                    binding.allButtonPreview.setOnClickListener {
                        openAllList(FilmMainListTypes.PREMIERS.type)
                    }

                    val popular =
                        viewModel.givePopularList(1, FilmMainListTypes.POPULAR_ALL_TIME.type)
                    val popularAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.POPULAR_ALL_TIME.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    popularAdapterForHomePage.checkTheNumberOfItems(popular.size)
                    popularAdapterForHomePage.addPopular(popular)
                    binding.recyclerPopular.adapter = popularAdapterForHomePage
                    if (premier.size < 20) binding.allButtonPopular.isEnabled = false
                    binding.allButtonPopular.setOnClickListener {
                        openAllList(FilmMainListTypes.POPULAR_ALL_TIME.type)
                    }

                    val usaAction = viewModel.giveFilteredList(
                        1,
                        FilmCountryFilters.USA.filter,
                        getString(R.string.genre_action)
                    )
                    val usaActionAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.FILTERED.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    usaActionAdapterForHomePage.checkTheNumberOfItems(usaAction.size)
                    usaActionAdapterForHomePage.addFiltered(usaAction)
                    binding.recyclerUsaAction.adapter = usaActionAdapterForHomePage
                    if (premier.size < 20) binding.allButtonUsaAction.isEnabled = false
                    binding.allButtonUsaAction.setOnClickListener {
                        openAllList(FilmMainListTypes.FILTERED.type)
                    }

                    val topMovies = viewModel.givePopularList(1, FilmMainListTypes.TOP_MOVIES.type)
                    val topMoviesAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.TOP_MOVIES.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    topMoviesAdapterForHomePage.checkTheNumberOfItems(topMovies.size)
                    topMoviesAdapterForHomePage.addPopular(topMovies)
                    binding.recyclerTop.adapter = topMoviesAdapterForHomePage
                    if (premier.size < 20) binding.allButtonTop.isEnabled = false
                    binding.allButtonTop.setOnClickListener {
                        openAllList(FilmMainListTypes.TOP_MOVIES.type)
                    }

                    val frenchDramas = viewModel.giveFilteredList(
                        1,
                        FilmCountryFilters.FRANCE.filter,
                        getString(R.string.genre_drama)
                    )
                    val frenchDramasAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.FRENCH_DRAMAS.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    frenchDramasAdapterForHomePage.checkTheNumberOfItems(frenchDramas.size)
                    frenchDramasAdapterForHomePage.addFiltered(frenchDramas)
                    binding.recyclerFrenchDramas.adapter = frenchDramasAdapterForHomePage
                    if (premier.size < 20) binding.allButtonFrenchDramas.isEnabled = false
                    binding.allButtonFrenchDramas.setOnClickListener {
                        openAllList(FilmMainListTypes.FRENCH_DRAMAS.type)
                    }

                    val tvSeries = viewModel.givePopularList(1, FilmMainListTypes.TOP_TV.type)
                    val tvSeriesAdapterForHomePage = AdapterForHomePage(
                        FilmMainListTypes.TOP_TV.type,
                        { id -> openMoviePage(id) }) { list -> openAllList(list) }
                    tvSeriesAdapterForHomePage.checkTheNumberOfItems(tvSeries.size)
                    tvSeriesAdapterForHomePage.addPopular(tvSeries)
                    binding.recyclerTVSeries.adapter = tvSeriesAdapterForHomePage
                    if (premier.size < 20) binding.allButtonTVSeries.isEnabled = false
                    binding.allButtonTVSeries.setOnClickListener {
                        openAllList(FilmMainListTypes.TOP_TV.type)
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

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action = HomepageFragmentDirections.actionHomepageFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

    private fun openAllList(list: String) {
        val action =
            HomepageFragmentDirections.actionHomepageFragmentToListOfEverythingFragment(list)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
