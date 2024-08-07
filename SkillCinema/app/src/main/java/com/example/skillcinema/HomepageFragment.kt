package com.example.skillcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.data.GenresAndCountries
import com.example.skillcinema.databinding.FragmentHomepageBinding
import kotlinx.coroutines.launch

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
            val premierAdapterForHomePage = AdapterForHomePage {openAllList()}
            premierAdapterForHomePage.checkTheNumberOfItems(premier.size)
            premierAdapterForHomePage.addPremiers(premier)
            binding.recyclerPreview.adapter = premierAdapterForHomePage
            binding.allButtonPreview.setOnClickListener {
                openAllList()
            }

            val popular = RetrofitInstance.searchDataApi.getPopular().items
            val popularAdapterForHomePage = AdapterForHomePage() {openAllList()}
            popularAdapterForHomePage.addPopular(popular)
            binding.recyclerPopular.adapter = popularAdapterForHomePage
            binding.allButtonPopular.setOnClickListener {
                openAllList()
            }

            val usaAction = RetrofitInstance.searchDataApi.getFilteredMovies().items
            val usaActionAdapterForHomePage = AdapterForHomePage() {openAllList()}
            usaActionAdapterForHomePage.addFiltered(usaAction)
            binding.recyclerUsaAction.adapter = usaActionAdapterForHomePage
            binding.allButtonUsaAction.setOnClickListener {
                openAllList()
            }

            val topMovies = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_MOVIES").items
            val topMoviesAdapterForHomePage = AdapterForHomePage {openAllList()}
            topMoviesAdapterForHomePage.checkTheNumberOfItems(topMovies.size)
            topMoviesAdapterForHomePage.addPopular(topMovies)
            binding.recyclerTop.adapter = topMoviesAdapterForHomePage
            binding.allButtonTop.setOnClickListener {
                openAllList()
            }

            val frenchDramas = RetrofitInstance.searchDataApi.getFilteredMovies(genres = arrayOf(
                getGenreByFilter("драма")
            ), countries = arrayOf(getCountryByFilter("Франция"))
            ).items
            val frenchDramasAdapterForHomePage = AdapterForHomePage() {openAllList()}
            frenchDramasAdapterForHomePage.addFiltered(frenchDramas)
            binding.recyclerFrenchDramas.adapter = frenchDramasAdapterForHomePage
            binding.allButtonFrenchDramas.setOnClickListener {
                openAllList()
            }

            val tvSeries = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_TV_SHOWS").items
            val tvSeriesAdapterForHomePage = AdapterForHomePage {openAllList()}
            tvSeriesAdapterForHomePage.checkTheNumberOfItems(tvSeries.size)
            tvSeriesAdapterForHomePage.addPopular(tvSeries)
            binding.recyclerTVSeries.adapter = tvSeriesAdapterForHomePage
            binding.allButtonTVSeries.setOnClickListener {
                openAllList()
            }
        }

    }

    fun openAllList() {
        findNavController().navigate(R.id.listOfEverythingFragment)
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
