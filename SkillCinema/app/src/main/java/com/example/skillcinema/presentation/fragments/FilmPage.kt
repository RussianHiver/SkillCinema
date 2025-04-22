package com.example.skillcinema.presentation.fragments

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.FilmEntity
import com.example.skillcinema.presentation.DataViewModel
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

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private lateinit var adapterActors: AdapterForFilmPage

    private lateinit var adapterStaff: AdapterForFilmPage

    private lateinit var adapterGallery: AdapterForGallery

    private lateinit var adapterSimilars: AdapterForSimilars

    private lateinit var movie: MovieDataDTO

    private var isCollapsed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                val args: FilmPageArgs by navArgs()



                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                        movie = viewModel.giveOneMovie(args.kinopoiskID)
                        val staff = viewModel.giveMovieStaffList(args.kinopoiskID)
                        val gallery = viewModel.giveFilmGalleryList(args.kinopoiskID, 1, "STILL")
                        val similars = viewModel.giveSimilars(args.kinopoiskID)

                        val watched = FilmEntity(
                            id = movie.kinopoiskId,
                            posterUrl = movie.posterUrl,
                            nameRu = movie.nameRu ?: movie.nameEn ?: "Неизвестный",
                            genre = movie.genres.first().genre,
                            rating = movie.ratingKinopoisk
                        )

                        viewModel.insertFilm(watched)

                        binding.addToFavoritesButton.setOnClickListener {
                            when {
                                binding.addToFavoritesButton.isChecked -> {
                                    viewModel.insertFilmForCollection(
                                        CollectionListFilms(
                                            1,
                                            movie.kinopoiskId
                                        )
                                    )
                                }

                                !binding.addToFavoritesButton.isChecked -> {
                                    viewModel.deleteFilmForCollection(
                                        CollectionListFilms(
                                            1,
                                            movie.kinopoiskId
                                        )
                                    )
                                }
                            }
                        }

                        binding.addToMustWatchButton.setOnClickListener {
                            when {
                                binding.addToMustWatchButton.isChecked -> {
                                    viewModel.insertFilmForCollection(
                                        CollectionListFilms(
                                            2,
                                            movie.kinopoiskId
                                        )
                                    )
                                }

                                !binding.addToMustWatchButton.isChecked -> {
                                    viewModel.deleteFilmForCollection(
                                        CollectionListFilms(
                                            2,
                                            movie.kinopoiskId
                                        )
                                    )
                                }
                            }
                        }

                        binding.addToWatchedButton.setOnClickListener {
                            when {
                                binding.addToWatchedButton.isChecked -> {
                                    viewModel.insertFilmForCollection(
                                        CollectionListFilms(
                                            3,
                                            movie.kinopoiskId
                                        )
                                    )
                                }

                                !binding.addToWatchedButton.isChecked -> {
                                    viewModel.deleteFilmForCollection(
                                        CollectionListFilms(
                                            3,
                                            movie.kinopoiskId
                                        )
                                    )
                                }
                            }

                        }

                        binding.shareButton.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, movie.webUrl)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(intent, null)
                            startActivity(shareIntent)
                        }

                        binding.menuButton.setOnClickListener {
                            dataViewModel.setMovieData(movie)
                            val bottomSheetFragment = BottomSheetFragment()
                            val bundle = Bundle()
                            bundle.putInt("ID", movie.kinopoiskId)
                            bottomSheetFragment.arguments = bundle
                            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
                        }

                        launch {
                            viewModel.allCollection.collect { collect ->
                                collect.elementAtOrNull(0)?.films?.forEach {
                                    if (it.id == movie.kinopoiskId) {
                                        binding.addToFavoritesButton.isChecked = true
                                    }
                                }
                                collect.elementAtOrNull(1)?.films?.forEach {
                                    if (it.id == movie.kinopoiskId) {
                                        binding.addToMustWatchButton.isChecked = true
                                    }
                                }
                                collect.elementAtOrNull(2)?.films?.forEach {
                                    if (it.id == movie.kinopoiskId) {
                                        binding.addToWatchedButton.isChecked = true
                                    }
                                }
                            }
                        }

                        if (movie.type == "TV_SERIES") {
                            binding.seasonsAndEpisodesTitleAndAllButton.isVisible = true
                            val numberOfSeasons = viewModel.getSeasonsAndEpisodes(args.kinopoiskID).total
                            var numberOfEpisodes = 0
                            viewModel.getSeasonsAndEpisodes(args.kinopoiskID).items.forEach { numberOfEpisodes += it.episodes.count() }
                            binding.amountOfSeasonsAndEpisodes.text = getSeasonsAndEpisodesCountString(
                                seasons = numberOfSeasons,
                                episodes = numberOfEpisodes
                            )

                            binding.seasonsAndEpisodesAllButton.setOnClickListener {
                                val action = FilmPageDirections.actionFilmPageToSeasonsAndEpisodesFragment(
                                    movie.nameRu ?: movie.nameEn ?: "Без имени", args.kinopoiskID
                                )
                                findNavController().navigate(action)
                            }
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
                            with(filmPremise) {
                                text = movie.description
                                setOnClickListener {
                                    if (isCollapsed) {
                                        this.maxLines = Int.MAX_VALUE
                                    } else {
                                        this.maxLines = 10
                                    }
                                    isCollapsed = !isCollapsed
                                }
                            }

                            filmPageLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                            filmPageLayout.layoutTransition.setDuration(300)

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
                        }
                    }
                }
            } else {
                BottomSheetErrorFragment().show(parentFragmentManager, "sheetError")
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

    private fun openStaffMemberPage(id: Int) {
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
            patternForOne.matches(episodesString) -> "$episodes серия"
            patternForTwoThreeFour.matches(episodesString) -> "$episodes серии"
            else -> "$episodes серий"
        }

        return "$seasonsString, $episodesString"

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}