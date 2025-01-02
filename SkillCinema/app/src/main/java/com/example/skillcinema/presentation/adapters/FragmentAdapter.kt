package com.example.skillcinema.presentation.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.skillcinema.presentation.fragments.BlankFragment
import com.example.skillcinema.presentation.fragments.PAGE_NUMBER


class FragmentAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = PAGES_AMOUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = BlankFragment()
        fragment.arguments = Bundle().apply {
            putInt(PAGE_NUMBER, position + 1)
        }
        return fragment
    }

    companion object {
        private const val PAGES_AMOUNT = 3
    }
}