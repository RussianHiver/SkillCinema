package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.StaffListDTO
import com.example.skillcinema.databinding.FragmentFilmPageBinding
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForFilmPage
import com.example.skillcinema.presentation.adapters.AdapterForGallery
import com.example.skillcinema.presentation.adapters.AdapterForSimilars
import kotlinx.coroutines.launch

class FilmPage : Fragment() {

    private var _binding: FragmentFilmPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
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

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val movie = viewModel.giveOneMovie(args.kinopoiskID)
                val staff = viewModel.giveMovieStaffList(args.kinopoiskID)
                val gallery = viewModel.giveFilmGalleryList(args.kinopoiskID, 1, "STILL")
                val similars = viewModel.giveSimilars(args.kinopoiskID)

                if (movie.type == "TV_SERIES") {
                    binding.seasonsAndEpisodesTitleAndAllButton.isVisible = true
                    val numberOfSeasons = viewModel.getSeasonsAndEpisodes(args.kinopoiskID).total
                    var numberOfEpisodes = 0
                    viewModel.getSeasonsAndEpisodes(args.kinopoiskID).items.forEach { numberOfEpisodes += it.episodes.count() }
                    binding.amountOfSeasonsAndEpisodes.text = getSeasonsAndEpisodesCountString(
                        seasons = numberOfSeasons,
                        episodes = numberOfEpisodes
                    )
                }

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
                    if (movie.ratingKinopoisk != null) {
                        filmRatingAndName.text = "${
                            String.format(
                                "%.1f",
                                movie.ratingKinopoisk
                            )
                        } ${movie.nameRu ?: movie.nameEn}"
                    } else {
                        filmRatingAndName.text = "${movie.nameRu ?: movie.nameEn}"
                    }
                    filmYearAndGenre.text = getString(
                        R.string.film_year_and_genre,
                        movie.year.toString(),
                        movie.genres.first().genre,
                        getSecondGenre(movie.genres.getOrNull(1))
                    )
                    val filmLength = movie.filmLength?.let { convertFilmLength(it) }
                    filmCountryLengthAndAge.text = filmLength?.let {
                        "${movie.countries.first().country}, ${
                            getString(
                                R.string.film_length, it.getValue("hours"), it.getValue("minutes")
                            )
                        }${convertMovieAgeRestriction(movie.ratingAgeLimits)}"

                    }
                    Glide.with(filmPhoto.context).load(movie.coverUrl ?: movie.posterUrl)
                        .into(filmPhoto)
                    Glide.with(movieLogo.context).load(movie.logoUrl).into(movieLogo)
                    filmPremise.text = movie.description

                    allActorsButton.text = getStringOfListForStaff(staff, true)
                    allActorsButton.setOnClickListener {
                        openListOfStaff(true, args.kinopoiskID)
                    }

                    filmStaffButton.text = getStringOfListForStaff(staff, false)
                    filmStaffButton.setOnClickListener {
                        openListOfStaff(false, args.kinopoiskID)
                    }

                    filmGalleryButton.text =
                        if (gallery.size <= 3) "${gallery.size}" else "${gallery.size} >"
                    filmGalleryButton.setOnClickListener {
                        openListOfPhotos(args.kinopoiskID)
                    }

                    similarFilmAll.text =
                        if (similars.total <= 3) "${similars.total}" else "${similars.total} >"
                    similarFilmAll.setOnClickListener {
                        openListSimilars(args.kinopoiskID)
                    }

                    recyclerFilmActors.adapter = adapterActors
                    recyclerStaff.adapter = adapterStaff
                    recyclerGallery.adapter = adapterGallery
                    recyclerSimilarFilm.adapter = adapterSimilars

                    binding.seasonsAndEpisodesAllButton.setOnClickListener {
                        val action = FilmPageDirections.actionFilmPageToSeasonsAndEpisodesFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }


    }

    private fun convertFilmLength(length: Int): Map<String, Int> {
        val hours = length / 60
        val minutes = length % 60
        return mapOf("hours" to hours, "minutes" to minutes)
    }

    private fun convertMovieAgeRestriction(ratings: String?): String {
        return if (ratings != null) {
            ", ${ratings.removeRange(0, 3)}+"
        } else {
            ""
        }
    }


    private fun getSecondGenre(genre: MovieDataDTO.Genre?): String {
        return if (genre != null) ", ${genre.genre}" else ""
    }

    private fun getStringOfListForStaff(list: List<StaffListDTO>, actors: Boolean): String {
        val newList: List<StaffListDTO> = if (actors) {
            list.filter { it.professionKey == "ACTOR" }
        } else {
            list.filter { it.professionKey != "ACTOR" }
        }
        return "${newList.size} >"
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

    private fun getSeasonsAndEpisodesCountString(seasons: Int, episodes: Int): String {

        var seasonsString = seasons.toString()
        var episodesString = episodes.toString()

        val patternForOne = Regex("[0-9]*[^1]?1$")
        val patternForTwoThreeFour = Regex("[0-9]*[^1]?[234]$")

        seasonsString = when {
            patternForOne.matches(seasonsString) -> "$seasons сезон"
            patternForTwoThreeFour.matches(seasonsString) -> "$seasons сезона"
            else -> "$seasons сезонов"
        }

        episodesString = when {
            patternForOne.matches(episodesString) -> "$episodes эпизод"
            patternForTwoThreeFour.matches(episodesString) -> "$episodes эпизода"
            else -> "$episodes эпизодов"
        }

        return "$seasonsString, $episodesString"

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}