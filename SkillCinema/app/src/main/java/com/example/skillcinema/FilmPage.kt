package com.example.skillcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.FragmentFilmPageBinding
import kotlinx.coroutines.launch

class FilmPage : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: AdapterForFilmPage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FilmPageArgs by navArgs()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val movie = RetrofitInstance.searchDataApi.getOneMovie(args.kinopoiskID)
                val staff = RetrofitInstance.searchDataApi.getFilmStaff(args.kinopoiskID)
                adapter = AdapterForFilmPage(staff)
                with(binding) {
                    filmRatingAndName.text = movie.nameRu ?: movie.nameEn ?: "No name"
                    filmYearAndGenre.text = getString(
                        R.string.film_year_and_genre,
                        movie.year.toString(),
                        movie.genres.first().genre
                    )
                    val filmLength = movie.filmLength?.let { convertFilmLength(it) }
                    filmCountryLengthAndAge.text = filmLength?.let {
                        getString(
                            R.string.film_length,
                            it.getValue("hours"),
                            it.getValue("minutes")
                        )
                    }
                    Glide.with(filmPhoto.context)
                        .load(movie.posterUrl)
                        .into(filmPhoto)
                    filmPremise.text = movie.description
                }
            }
        }


    }

    fun convertFilmLength(length: Int): Map<String, Int> {
        val hours = length / 60
        val minutes = length % 60
        return mapOf("hours" to hours, "minutes" to minutes)
    }

}