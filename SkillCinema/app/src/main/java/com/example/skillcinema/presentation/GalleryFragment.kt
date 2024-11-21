package com.example.skillcinema.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.data.AdapterForFullGallery
import com.example.skillcinema.databinding.FragmentGalleryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val args: GalleryFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels()

    private var adapter: AdapterForFullGallery = AdapterForFullGallery()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId = args.FilmId
        viewModel.filmId.value = filmId
        binding.fullListOfPhotos.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingDataGallery.collectLatest {
                    adapter.submitData(it)
                }
                binding.chipStillImages.setOnClickListener { viewModel.imagesType.value = "STILL" }
                binding.chipShootingImages.setOnClickListener { viewModel.imagesType.value = "SHOOTING" }
                binding.chipPosterImages.setOnClickListener { viewModel.imagesType.value = "POSTER" }
            }
        }
    }
}