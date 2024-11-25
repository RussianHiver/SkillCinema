package com.example.skillcinema.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.AdapterForSearchResult
import com.example.skillcinema.databinding.FragmentSearchPageBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPageFragment : Fragment() {

    var _binding: FragmentSearchPageBinding? = null
    val binding: FragmentSearchPageBinding get() = _binding!!

    val viewModel: MainViewModel by viewModels()

    val adapter: AdapterForSearchResult = AdapterForSearchResult { id -> openMoviePage(id) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingDataSearchResult?.collectLatest { data ->
                    adapter.submitData(data)
                }
            }
        }

        binding.searchTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchTextInputLayout.hint = null
            } else {
                binding.searchTextInputLayout.hint = resources.getString(R.string.search_hint)
            }
        }

        binding.searchTextInputEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                viewModel.getNewPagerForSearchResult(it.toString())
            }
        }

        binding.searchTextInputLayout.setEndIconOnClickListener { findNavController().navigate(R.id.searchFilterFragment) }
    }

    fun openMoviePage(id: Int?) {
        id?.let {
            val action = SearchPageFragmentDirections.actionSearchPageFragmentToFilmPage(id)
            findNavController().navigate(action)
        }

    }
}