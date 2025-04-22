package com.example.skillcinema.presentation.fragments

import android.content.Context
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
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.databinding.FragmentStaffFilmographyBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterListOfAllStaffFilms
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StaffFilmographyFragment : Fragment() {

    private var _binding: FragmentStaffFilmographyBinding? = null
    private val binding get() = _binding!!

    private lateinit var staffMember: StaffMemberDTO

    private lateinit var chipGroup: ChipGroup

    private lateinit var fullFilmList: List<StaffMemberDTO.Film>

    private var neededFilmList: List<MovieDataDTO>? = null

    private val sortedMapOfMovieLists = mutableMapOf<String, List<MovieDataDTO>>()

    private val adapter by lazy { AdapterListOfAllStaffFilms { id -> openMoviePage(id) } }

    private var job = Job()

    private val viewmodel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                val args: StaffFilmographyFragmentArgs by navArgs()

                var previouslyChecked: Chip? = null

                binding.filmsByOccupationList.adapter = adapter

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        dataViewModel.filmography.observe(viewLifecycleOwner) {
                            fullFilmList = it
                        }
                        staffMember = viewmodel.giveIndividualStaffMember(args.StaffID)
                        chipGroup = binding.staffOccupationChipGroup
                        val occupations = staffMember.profession?.split(", ")?.toTypedArray()
                        with(binding) {
                            staffName.text =
                                staffMember.nameRu ?: staffMember.nameEn ?: "Неизвестный"
                            occupations?.let {
                                it.forEach { occupation ->
                                    chipGroup.addView(makeAChip(occupation))
                                }
                            }
                        }
                        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                            previouslyChecked?.isClickable = true
                            previouslyChecked = group.findViewById(checkedIds.first())
                            previouslyChecked?.isClickable = false
                            val occupation =
                                group.findViewById<Chip>(checkedIds.first()).text.toString()
                            viewLifecycleOwner.lifecycleScope.launch(job) {
                                binding.searchPageProgress.isVisible = true
                                val searchOccupation = getNeededOccupation(occupation)
                                getNeededFilmList(searchOccupation)
                                job.cancel()
                                job = Job()
                                binding.searchPageProgress.isVisible = false
                            }

                        }
                    }
                }
            } else {
                BottomSheetErrorFragment().show(parentFragmentManager, "sheetError")
            }
        }


    }

    private fun makeAChip(occupation: String): Chip {
        val chip = Chip(context)
        context?.let {
            val chipDrawable =
                ChipDrawable.createFromAttributes(it, null, 0, R.style.CustomChipChoice)
            with(chip) {
                isCheckable = true
                text = occupation
                setChipDrawable(chipDrawable)
                setTextColor(resources.getColorStateList(R.color.chip_state_text, null))
                textSize = 18f
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                chipStrokeWidth = 3f
                isChecked = false
            }
        }
        return chip
    }

    private fun getNeededOccupation(occupation: String): String {
        return when (occupation) {
            "Актер" -> "ACTOR"
            "Сценарист" -> "WRITER"
            "Продюсер" -> "PRODUCER"
            "Композитор" -> "COMPOSER"
            "Оператор" -> "OPERATOR"
            "Монтажер" -> "EDITOR"
            "Переводчик" -> "TRANSLATOR"
            "Режиссер" -> "DIRECTOR"
            "Режиссер дубляжа" -> "VOICE_DIRECTOR"
            "Художник" -> "DESIGN"
            "Директор фильма" -> "PRODUCER_USSR"
            else -> "UNKNOWN"
        }
    }

    private suspend fun getNeededFilmList(occupation: String) {
        if (sortedMapOfMovieLists.containsKey(occupation)) {
            val occupationList = sortedMapOfMovieLists[occupation]
            neededFilmList = occupationList
            adapter.submitList(neededFilmList)
        } else {
            val filteredFilmList = fullFilmList.filter { it.professionKey == occupation }
            val fullFilmInfoList = mutableListOf<MovieDataDTO>()
            filteredFilmList.forEach { film ->
                fullFilmInfoList.add(viewmodel.giveOneMovie(film.filmId))
            }
            sortedMapOfMovieLists[occupation] = fullFilmInfoList.toList()
            neededFilmList = fullFilmInfoList.toList()
            adapter.submitList(neededFilmList)
        }
    }

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action =
                StaffFilmographyFragmentDirections.actionStaffFilmographyFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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