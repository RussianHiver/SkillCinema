package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.skillcinema.databinding.FragmentGalleryBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForFullGallery
import com.example.skillcinema.presentation.adapters.MovieDataLoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val args: GalleryFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels {
        DaggerAppComponent.create().mainViewModelFactory()
    }

    private var adapter: AdapterForFullGallery = AdapterForFullGallery()

    private var stillImages = Job()

    private var shootingImages = Job()

    private var posterImages = Job()

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

        adapter.withLoadStateHeaderAndFooter(
            header = MovieDataLoadStateAdapter { adapter.retry() },
            footer = MovieDataLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.fullListOfPhotos.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.searchPageProgress.isVisible = loadState.source.refresh is LoadState.Loading
            binding.searchPageRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }

        binding.searchPageRetryButton.setOnClickListener {
            adapter.retry()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            binding.chipStillImages.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch(stillImages) {
                    viewModel.pagingDataGalleryStill.collectLatest {
                        shootingImages.cancel()
                        posterImages.cancel()
                        shootingImages = Job()
                        posterImages = Job()
                        adapter.submitData(it)
                    }
                }
            }

            binding.chipShootingImages.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch(shootingImages) {
                    viewModel.pagingDataGalleryShooting.collectLatest {
                        stillImages.cancel()
                        posterImages.cancel()
                        stillImages = Job()
                        posterImages = Job()
                        adapter.submitData(it)
                    }
                }
            }

            binding.chipPosterImages.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch(posterImages) {
                    viewModel.pagingDataGalleryPoster.collectLatest {
                        stillImages.cancel()
                        shootingImages.cancel()
                        stillImages = Job()
                        shootingImages = Job()
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    fun handleError(loadstate: CombinedLoadStates) {
        val errorState = loadstate.source.append as? LoadState.Error
            ?: loadstate.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(activity, "${it.error}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}