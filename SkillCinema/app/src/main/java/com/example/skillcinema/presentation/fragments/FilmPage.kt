package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentFilmPageBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForFilmPage
import com.example.skillcinema.presentation.adapters.AdapterForGallery
import com.example.skillcinema.presentation.adapters.AdapterForSimilars
import kotlinx.coroutines.launch

class FilmPage : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        DaggerAppComponent.create().mainViewModelFactory()
    }

    private lateinit var adapterActors: AdapterForFilmPage

    private lateinit var adapterStaff: AdapterForFilmPage

    private lateinit var adapterGallery: AdapterForGallery

    private lateinit var adapterSimilars: AdapterForSimilars
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FilmPageArgs by navArgs()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val movie = viewModel.giveOneMovie(args.kinopoiskID)
                val staff = viewModel.giveMovieStaffList(args.kinopoiskID)
                val gallery = viewModel.giveFilmGalleryList(args.kinopoiskID, 1, "STILL")
                val similars = viewModel.giveSimilars(args.kinopoiskID)
                adapterStaff =
                    AdapterForFilmPage(staff, areActors = false, fullList = false) { id ->
                        openStaffMemberPage(
                            id
                        )
                    }
                adapterActors =
                    AdapterForFilmPage(staff, areActors = true, fullList = false) { id ->
                        openStaffMemberPage(
                            id
                        )
                    }
                adapterGallery = AdapterForGallery(gallery)
                adapterSimilars = AdapterForSimilars(similars, false)

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
                            R.string.film_length, it.getValue("hours"), it.getValue("minutes")
                        )
                    }
                    Glide.with(filmPhoto.context).load(movie.posterUrl).into(filmPhoto)
                    filmPremise.text = movie.description

                    allActorsButton.setOnClickListener {
                        openListOfStaff(true, args.kinopoiskID)
                    }

                    filmStaffButton.setOnClickListener {
                        openListOfStaff(false, args.kinopoiskID)
                    }

                    filmGalleryButton.setOnClickListener {
                        openListOfPhotos(args.kinopoiskID)
                    }

                    similarFilmAll.setOnClickListener {
                        openListSimilars(args.kinopoiskID)
                    }

                    recyclerFilmActors.adapter = adapterActors
                    recyclerStaff.adapter = adapterStaff
                    recyclerGallery.adapter = adapterGallery
                    recyclerSimilarFilm.adapter = adapterSimilars
                }
            }
        }


    }

    private fun convertFilmLength(length: Int): Map<String, Int> {
        val hours = length / 60
        val minutes = length % 60
        return mapOf("hours" to hours, "minutes" to minutes)
    }

    private fun openListOfStaff(actorsOrNot: Boolean, id: Int) {
        val action = FilmPageDirections.actionFilmPageToListOfStaffFragment(actorsOrNot, id)
        findNavController().navigate(action)
    }

    private fun openListOfPhotos(filmId: Int) {
        val action = FilmPageDirections.actionFilmPageToGalleryFragment(filmId)
        findNavController().navigate(action)
    }

    private fun openListSimilars(filmId: Int) {
        val action = FilmPageDirections.actionFilmPageToSimilarsFragment(filmId)
        findNavController().navigate(action)
    }

    fun openStaffMemberPage(id: Int) {
        val action = FilmPageDirections.actionFilmPageToActorPageFragment(id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}