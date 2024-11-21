package com.example.skillcinema.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.data.AdapterForSimilars
import com.example.skillcinema.data.RetrofitInstance
import com.example.skillcinema.data.Similars
import com.example.skillcinema.databinding.FragmentListOfSimilarsBinding
import kotlinx.coroutines.launch


class ListOfSimilarsFragment : Fragment() {

    private var _binding: FragmentListOfSimilarsBinding? = null
    private val binding get() = _binding!!

    private val args: ListOfSimilarsFragmentArgs by navArgs()

    private lateinit var similars: Similars

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
                similars = RetrofitInstance.searchDataApi.getSimilars(filmId)
                binding.recyclerAllSimilars.adapter = adapter
            }
        }
    }

}