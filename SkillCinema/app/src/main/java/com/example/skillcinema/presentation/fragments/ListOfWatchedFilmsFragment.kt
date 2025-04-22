package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentListOfWatchedFilmsBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.adapters.AdapterForListOfAllWatchedFilms
import kotlinx.coroutines.launch


class ListOfWatchedFilmsFragment : Fragment() {

    private var _binding: FragmentListOfWatchedFilmsBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private val adapter by lazy { AdapterForListOfAllWatchedFilms { id -> openMoviePage(id) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfWatchedFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.listOfEverythingRecycler.adapter = adapter

                dataViewModel.watchedFilmsList.observe(viewLifecycleOwner) { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action =
                ListOfWatchedFilmsFragmentDirections.actionListOfWatchedFilmsFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

}