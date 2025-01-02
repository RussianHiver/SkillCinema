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
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.data.SimilarsDTO
import com.example.skillcinema.databinding.FragmentListOfSimilarsBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.AdapterForSimilars
import kotlinx.coroutines.launch


class ListOfSimilarsFragment : Fragment() {

    private var _binding: FragmentListOfSimilarsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels{
        DaggerAppComponent.create().mainViewModelFactory()
    }

    private val args: ListOfSimilarsFragmentArgs by navArgs()

    private lateinit var similars: SimilarsDTO

    private val adapter: AdapterForSimilars by lazy { AdapterForSimilars(similars, true) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfSimilarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId = args.FilmId

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                similars = viewModel.giveSimilars(id = filmId)
                binding.recyclerAllSimilars.adapter = adapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}