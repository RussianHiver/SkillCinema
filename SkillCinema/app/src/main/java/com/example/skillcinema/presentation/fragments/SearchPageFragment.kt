package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSearchPageBinding
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForSearchResult
import com.example.skillcinema.presentation.adapters.MovieDataLoadStateAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPageFragment : Fragment() {

    var _binding: FragmentSearchPageBinding? = null
    val binding: FragmentSearchPageBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    val adapter = AdapterForSearchResult({ id -> openMoviePage(id) })

    private var searchJob = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: SearchPageFragmentArgs by navArgs()

        binding.searchResultRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MovieDataLoadStateAdapter { adapter.retry() },
            footer = MovieDataLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.searchResultRecycler.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.stateProgress.isVisible = loadState.source.refresh is LoadState.Loading
            binding.stateRetryButton.isVisible = loadState.source.refresh is LoadState.Error
            handleError(loadState)
        }

        binding.searchTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchTextInputLayout.hint = null
            } else {
                binding.searchTextInputLayout.hint = resources.getString(R.string.search_hint)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingDataSearchResult.collectLatest {
                    it?.let {
                        adapter.submitData(it)
                    }
                }
            }
        }


        binding.searchTextInputEditText.doOnTextChanged { text, _, _, _ ->
            text?.let {
                viewLifecycleOwner.lifecycleScope.launch(searchJob) {
                    var fixedGenre = args.genre
                    fixedGenre?.let { string ->
                        fixedGenre = string.lowercase()
                    }
                    viewModel.getNewPagerForSearchResult(
                        text.toString(),
                        country = args.country ?: DEFAULT_COUNTRY,
                        genre = fixedGenre ?: DEFAULT_GENRE,
                        type = args.type ?: DEFAULT_TYPE,
                        order = args.sort ?: DEFAULT_ORDER,
                        ratingFrom = args.ratingFrom,
                        ratingTo = args.ratingTo
                    )
                    searchJob.cancel()
                    searchJob = Job()
                }
            }
        }


        binding.searchTextInputLayout.setEndIconOnClickListener {
            val action =
                SearchPageFragmentDirections.actionSearchPageFragmentToSearchFilterFragment(
                    null,
                    null
                )
            findNavController().navigate(action)
        }
    }

    fun openMoviePage(id: Int?) {
        id?.let {
            val action = SearchPageFragmentDirections.actionSearchPageFragmentToFilmPage(id)
            findNavController().navigate(action)
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val state = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        state?.let {
            Toast.makeText(activity, "${state.error}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val DEFAULT_COUNTRY = "США"
        private const val DEFAULT_GENRE = "боевик"
        private const val DEFAULT_TYPE = "ALL"
        private const val DEFAULT_ORDER = "RATING"
    }

}