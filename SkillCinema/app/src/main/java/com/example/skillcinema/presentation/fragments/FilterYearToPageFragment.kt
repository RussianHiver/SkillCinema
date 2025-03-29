package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentFilterYearToPageBinding
import com.example.skillcinema.presentation.DataViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup


class FilterYearToPageFragment : Fragment() {

    private var _binding: FragmentFilterYearToPageBinding? = null
    private val binding get() = _binding!!

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var chipGroup: ChipGroup
    private var pages: Int = 1
    private var pageNumber: Int = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterYearToPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = binding.filterYearChipGroup

        arguments?.takeIf { it.containsKey(FILTER_PAGE_NUMBER) }?.apply {
            pageNumber = getInt(FILTER_PAGE_NUMBER)
        }

        arguments?.takeIf { it.containsKey(FILTER_PAGES_AMOUNT) }?.apply {
            pages = getInt(FILTER_PAGES_AMOUNT)
        }

        binding.filterYearPageText.text = "$pageNumber из $pages"

        arguments?.takeIf { it.containsKey(FILTER_YEAR_LIST) }?.apply {
            val list = getIntegerArrayList(FILTER_YEAR_LIST)?.toList()
            val firstYear = list?.first()
            val lastYear = list?.last()
            binding.filterYearPeriodText.text = "$firstYear - $lastYear"
            list?.forEach { year ->
                chipGroup.addView(makeAChip(year))
            }
        }

        var previouslyChecked: Chip? = null

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            previouslyChecked?.isClickable = true
            previouslyChecked = group.findViewById(checkedIds.first())
            previouslyChecked?.isClickable = false
            dataViewModel.setYearToFilter(previouslyChecked?.text?.toString()?.toInt() ?: 2024)
            var yearTo = previouslyChecked?.text?.toString()?.toInt() ?: 2024
            editor.apply {
                val yearFrom = sharedPreferences.getInt("filterYearFrom", 2024)
                if (yearFrom > yearTo) yearTo = yearFrom
                putInt("filterYearTo", yearTo)
            }.apply()
        }

    }

    private fun makeAChip(year: Int): Chip {
        val chip = Chip(context)
        context?.let {
            val chipDrawable =
                ChipDrawable.createFromAttributes(it, null, 0, R.style.CustomChipChoice)
            with(chip) {
                isCheckable = true
                text = year.toString()
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}