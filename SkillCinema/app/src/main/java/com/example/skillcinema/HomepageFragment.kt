package com.example.skillcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
            val premierAdapter = Adapter()
            premierAdapter.addPremiers(premier)
            binding.recyclerPreview.adapter = premierAdapter

            val popular = RetrofitInstance.searchDataApi.getPopular().items
            val popularAdapter = Adapter()
            popularAdapter.addPopular(popular)
            binding.recyclerPopular.adapter = popularAdapter

            val usaAction = RetrofitInstance.searchDataApi.getFilteredMovies().items
            val usaActionAdapter = Adapter()
            usaActionAdapter.addFiltered(usaAction)
            binding.recyclerUsaAction.adapter = usaActionAdapter

            val topMovies = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_MOVIES").items
            val topMoviesAdapter = Adapter()
            topMoviesAdapter.addPopular(topMovies)
            binding.recyclerTop.adapter = topMoviesAdapter

            val frenchDramas = RetrofitInstance.searchDataApi.getFilteredMovies(genres = arrayOf(
                getGenreByFilter("драма")
            ), countries = arrayOf(getCountryByFilter("Франция"))
            ).items
            val frenchDramasAdapter = Adapter()
            frenchDramasAdapter.addFiltered(frenchDramas)
            binding.recyclerFrenchDramas.adapter = frenchDramasAdapter

            val tvSeries = RetrofitInstance.searchDataApi.getPopular(type = "TOP_250_TV_SHOWS").items
            val tvSeriesAdapter = Adapter()
            tvSeriesAdapter.addPopular(tvSeries)
            binding.recyclerTVSeries.adapter = tvSeriesAdapter
        }
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
