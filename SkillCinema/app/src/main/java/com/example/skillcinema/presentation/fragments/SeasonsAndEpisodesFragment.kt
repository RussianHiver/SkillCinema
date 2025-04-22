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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSeasonsAndEpisodesBinding
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForEpisodesList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SeasonsAndEpisodesFragment : Fragment() {

    private var _binding: FragmentSeasonsAndEpisodesBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private lateinit var chipGroup: ChipGroup

    private var job = Job()

    private val adapter: AdapterForEpisodesList by lazy { AdapterForEpisodesList() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsAndEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        context?.let { context ->
            if (checkForInternet(context)) {
                val args: SeasonsAndEpisodesFragmentArgs by navArgs()


                binding.headerTitle.text = args.TVSeriesName

                binding.recyclerEpisodes.adapter = adapter

                var previouslyChecked: Chip? = null

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        chipGroup = binding.amountOfSeasons
                        val seasonsAndEpisodesDTO = viewModel.getSeasonsAndEpisodes(args.TVSeriesID)
                        repeat(seasonsAndEpisodesDTO.total) { attempt ->
                            chipGroup.addView(makeAChip(attempt + 1))
                        }

                        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                            previouslyChecked?.isClickable = true
                            previouslyChecked = group.findViewById(checkedIds.first())
                            previouslyChecked?.isClickable = false
                            val seasonNumber = previouslyChecked?.text.toString().toInt()
                            viewLifecycleOwner.lifecycleScope.launch(job) {
                                binding.episodesListProgressBar.isVisible = true
                                val amountOfEpisodes =
                                    seasonsAndEpisodesDTO.items[seasonNumber - 1].episodes.count()
                                binding.seasonsAndAmountOfEpisodes.text =
                                    getSeasonsAndEpisodesCountString(seasonNumber, amountOfEpisodes)
                                adapter.submitList(seasonsAndEpisodesDTO.items[seasonNumber - 1].episodes)
                                job.cancel()
                                job = Job()
                                binding.episodesListProgressBar.isVisible = false
                            }
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

    private fun makeAChip(number: Int): Chip {
        val chip = Chip(context)
        context?.let {
            val chipDrawable =
                ChipDrawable.createFromAttributes(it, null, 0, R.style.CustomChipChoice)
            with(chip) {
                isCheckable = true
                text = number.toString()
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

    private fun getSeasonsAndEpisodesCountString(seasons: Int, episodes: Int): String {

        val seasonsString = seasons.toString()
        var episodesString = episodes.toString()

        val patternForOne = Regex("[0-9]*[^1]?1$")
        val patternForTwoThreeFour = Regex("[0-9]*[^1]?[234]$")

        episodesString = when {
            patternForOne.matches(episodesString) -> "$episodes серия"
            patternForTwoThreeFour.matches(episodesString) -> "$episodes серии"
            else -> "$episodes серий"
        }

        return "$seasonsString сезон, $episodesString"

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

