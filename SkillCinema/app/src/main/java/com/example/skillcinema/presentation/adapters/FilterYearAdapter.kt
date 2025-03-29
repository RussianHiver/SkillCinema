package com.example.skillcinema.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinema.presentation.fragments.FILTER_PAGES_AMOUNT
import com.example.skillcinema.presentation.fragments.FILTER_PAGE_NUMBER
import com.example.skillcinema.presentation.fragments.FILTER_YEAR_LIST
import com.example.skillcinema.presentation.fragments.FilterYearFromPageFragment
import com.example.skillcinema.presentation.fragments.FilterYearToPageFragment

class FilterYearAdapter(
    fragment: FragmentActivity,
    private val years: IntRange,
    private val isYearFrom: Boolean
) :
    FragmentStateAdapter(fragment) {

    private var pages: Int = 0
    private var yearsList = years.toList()
    private lateinit var yearsChipCount: List<Int>

    override fun getItemCount(): Int {
        val yearsRemainder = years.count() % 12
        val yearsSubtraction = years.count() / 12
        if (yearsRemainder == 0) {
            pages = yearsSubtraction
            return yearsSubtraction
        } else {
            pages = yearsSubtraction + 1
            return yearsSubtraction + 1
        }
    }


    override fun createFragment(position: Int): Fragment {

        if (yearsList.count() >= 12) {
            yearsChipCount = yearsList.take(12)
            yearsList = yearsList.filter { it !in yearsChipCount }
        } else {
            yearsChipCount = yearsList.take(yearsList.count())
        }

        val fragment = if (isYearFrom) FilterYearFromPageFragment() else FilterYearToPageFragment()
        fragment.arguments = Bundle().apply {
            putInt(FILTER_PAGE_NUMBER, position + 1)
            putInt(FILTER_PAGES_AMOUNT, pages)
            putIntegerArrayList(FILTER_YEAR_LIST, ArrayList(yearsChipCount))
        }
        return fragment
    }

}