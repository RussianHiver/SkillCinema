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
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSearchFilterGenreBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForFilters
import kotlinx.coroutines.launch


class SearchFilterGenreFragment : Fragment() {

    private var _binding: FragmentSearchFilterGenreBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterForFilters

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerBackButton.setOnClickListener {
            val action =
                SearchFilterGenreFragmentDirections.actionSearchFilterGenreFragmentToSearchFilterFragment(
                    null,
                    null
                )
            findNavController().navigate(action)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val filters = viewModel.giveFilters()
                adapter = AdapterForFilters(false, filters) { filter -> sendChosenGenre(filter) }
                binding.filterGenreRecycler.adapter = adapter
            }
        }

    }

    private fun sendChosenGenre(chosenGenre: String) {
        val action =
            SearchFilterGenreFragmentDirections.actionSearchFilterGenreFragmentToSearchFilterFragment(
                null,
                chosenGenre
            )
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}