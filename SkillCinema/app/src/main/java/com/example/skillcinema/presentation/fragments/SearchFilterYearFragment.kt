package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentSearchFilterYearBinding
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.adapters.FilterYearAdapter
import java.time.Year


class SearchFilterYearFragment : Fragment() {

    private var _binding: FragmentSearchFilterYearBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private lateinit var viewPagerYearFrom: ViewPager2
    private lateinit var viewPagerYearTo: ViewPager2

    private lateinit var adapterYearFrom: FilterYearAdapter
    private lateinit var adapterYearTo: FilterYearAdapter

    private val currentYear = Year.now().value
    private val years = (1998..currentYear)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var yearFromFilter = 1998
        var yearToFilter = 2024

        with(dataViewModel) {
            yearFrom.observe(viewLifecycleOwner) {
                yearFromFilter = it
            }

            yearTo.observe(viewLifecycleOwner) {
                yearToFilter = it
            }
        }

        viewPagerYearFrom = binding.pagerYearFrom
        viewPagerYearTo = binding.pagerYearTo
        adapterYearFrom = FilterYearAdapter(requireActivity(), years, true)
        adapterYearTo = FilterYearAdapter(requireActivity(), years,false)

        viewPagerYearFrom.adapter = adapterYearFrom
        viewPagerYearTo.adapter = adapterYearTo

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.filterYearChooseButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}