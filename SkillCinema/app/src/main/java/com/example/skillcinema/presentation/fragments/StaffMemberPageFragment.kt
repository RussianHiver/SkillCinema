package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.data.App
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.databinding.FragmentActorPageBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForStaffBestFilms
import kotlinx.coroutines.launch

class StaffMemberPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null
    private val binding get() = _binding!!

    private val args: StaffMemberPageFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }


    private val bestFilmList = mutableListOf<MovieDataDTO>()

    private val adapter by lazy { AdapterForStaffBestFilms(bestFilmList) }


    private lateinit var staffMember: StaffMemberDTO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                viewLifecycleOwner.lifecycleScope.launch {

                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        staffMember = viewModel.giveIndividualStaffMember(id = args.PersonId)
                        with(binding) {
                            Glide.with(actorPhoto.context).load(staffMember.posterUrl)
                                .into(actorPhoto)
                            name.text = staffMember.nameRu ?: staffMember.nameEn ?: "Без названия"
                            occupation.text = staffMember.profession
                            amountOfFilmsInFilmography.text =
                                getFilmCountString(staffMember.films.size)
                        }

                        val sortedByRatingList = staffMember.films.sortedByDescending { it.rating }
                        val bestFilmIdList = sortFilmByRating(sortedByRatingList)

                        bestFilmIdList.forEach {
                            bestFilmList.add(viewModel.giveOneMovie(it))
                        }

                        binding.actorBestFilmsRecycler.adapter = adapter

                        binding.actorPhoto.setOnClickListener {
                            val action =
                                StaffMemberPageFragmentDirections.actionActorPageFragmentToStaffPhotoFullFragment(
                                    staffMember.posterUrl
                                )
                            findNavController().navigate(action)
                        }

                        binding.filmographyButton.setOnClickListener {
                            dataViewModel.setFilmography(sortedByRatingList)
                            val action =
                                StaffMemberPageFragmentDirections.actionActorPageFragmentToStaffFilmographyFragment(
                                    staffMember.staffId
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            } else {
                BottomSheetErrorFragment().show(parentFragmentManager, "sheetError")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun sortFilmByRating(filmList: List<StaffMemberDTO.Film>): IntArray {

        val idAdded = mutableListOf<Int>()

        run breaking@{
            filmList.forEach { film ->
                when {
                    idAdded.size > 9 -> return@breaking
                    film.filmId in idAdded -> return@forEach
                    else -> idAdded.add(film.filmId)
                }
            }
        }

        return idAdded.toIntArray()
    }

    private fun getFilmCountString(films: Int): String {
        return when (films) {
            1 -> "$films фильм"
            in (2..4) -> "$films фильма"
            else -> "$films фильмов"
        }
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