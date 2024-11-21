package com.example.skillcinema.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.data.AdapterForHomePage
import com.example.skillcinema.data.GenresAndCountries
import com.example.skillcinema.data.RetrofitInstance
import com.example.skillcinema.databinding.FragmentHomepageBinding
import kotlinx.coroutines.launch

private const val PREMIERS = "premiers"
private const val POPULAR_ALL_TIME = "popular"
private const val TOP_MOVIES = "TOP_250_MOVIES"
private const val FILTERED = "filtered"
private const val FRENCH_DRAMAS = "french_dramas"
private const val TOP_TV = "TOP_250_TV_SHOWS"
class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

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
            val premier = RetrofitInstance.searchDataApi.getMoviePremieres().items
            val premierAdapterForHomePage = AdapterForHomePage(PREMIERS, { id -> openMoviePage(id)}) { list -> openAllList(list)}
            premierAdapterForHomePage.checkTheNumberOfItems(premier.size)
            premierAdapterForHomePage.addPremiers(premier)
            binding.recyclerPreview.adapter = premierAdapterForHomePage
            if (premier.size < 20) binding.allButtonPreview.isEnabled = false
            binding.allButtonPreview.setOnClickListener {
                openAllList(PREMIERS)
            }

            val popular = RetrofitInstance.searchDataApi.getPopular().items
            val popularAdapterForHomePage = AdapterForHomePage(POPULAR_ALL_TIME, { id -> openMoviePage(id)}) { list -> openAllList(list)}
            popularAdapterForHomePage.checkTheNumberOfItems(popular.size)
            popularAdapterForHomePage.addPopular(popular)
            binding.recyclerPopular.adapter = popularAdapterForHomePage
            if (premier.size < 20) binding.allButtonPopular.isEnabled = false
            binding.allButtonPopular.setOnClickListener {
                openAllList(POPULAR_ALL_TIME)
            }

            val usaAction = RetrofitInstance.searchDataApi.getFilteredMovies().items
            val usaActionAdapterForHomePage = AdapterForHomePage(FILTERED, { id -> openMoviePage(id)}) { list -> openAllList(list)}
            usaActionAdapterForHomePage.checkTheNumberOfItems(usaAction.size)
            usaActionAdapterForHomePage.addFiltered(usaAction)
            binding.recyclerUsaAction.adapter = usaActionAdapterForHomePage
            if (premier.size < 20) binding.allButtonUsaAction.isEnabled = false
            binding.allButtonUsaAction.setOnClickListener {
                openAllList(FILTERED)
            }

            val topMovies = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_MOVIES").items
            val topMoviesAdapterForHomePage = AdapterForHomePage(POPULAR_ALL_TIME, { id -> openMoviePage(id)}) { list -> openAllList(list)}
            topMoviesAdapterForHomePage.checkTheNumberOfItems(topMovies.size)
            topMoviesAdapterForHomePage.addPopular(topMovies)
            binding.recyclerTop.adapter = topMoviesAdapterForHomePage
            if (premier.size < 20) binding.allButtonTop.isEnabled = false
            binding.allButtonTop.setOnClickListener {
                openAllList(TOP_MOVIES)
            }

            val frenchDramas = RetrofitInstance.searchDataApi.getFilteredMovies(genres = arrayOf(
                getGenreByFilter("драма")
            ), countries = arrayOf(getCountryByFilter("Франция"))
            ).items
            val frenchDramasAdapterForHomePage = AdapterForHomePage(FILTERED, { id -> openMoviePage(id)}) { list -> openAllList(list)}
            frenchDramasAdapterForHomePage.checkTheNumberOfItems(frenchDramas.size)
            frenchDramasAdapterForHomePage.addFiltered(frenchDramas)
            binding.recyclerFrenchDramas.adapter = frenchDramasAdapterForHomePage
            if (premier.size < 20) binding.allButtonFrenchDramas.isEnabled = false
            binding.allButtonFrenchDramas.setOnClickListener {
                openAllList(FRENCH_DRAMAS)
            }

            val tvSeries = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_TV_SHOWS").items
            val tvSeriesAdapterForHomePage = AdapterForHomePage(POPULAR_ALL_TIME, { id -> openMoviePage(id)}) { list -> openAllList(list)}
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
        val action = HomepageFragmentDirections.actionHomepageFragmentToListOfEverythingFragment(list)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
suspend fun getGenreByFilter(genre: String = "боевик"): Int {
    val filters = RetrofitInstance.searchDataApi.getFilters()
    val neededGenre: List<GenresAndCountries.Genre> = filters.genres.filter { it.genre == genre }
    return neededGenre.first().id
}

suspend fun getCountryByFilter(country: String = "США"): Int {
    val filters = RetrofitInstance.searchDataApi.getFilters()
    val neededCountry: List<GenresAndCountries.Country> = filters.countries.filter { it.country == country }
    return neededCountry.first().id
}
