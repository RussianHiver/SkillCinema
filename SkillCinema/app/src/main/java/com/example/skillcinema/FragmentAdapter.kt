package com.example.skillcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val PAGES_AMOUNT = 3

class FragmentAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = PAGES_AMOUNT

    override fun createFragment(position: Int): Fragment {
        val fragment = BlankFragment()
        fragment.arguments = Bundle().apply {
            putInt(PAGE_NUMBER, position + 1)
        }
        return fragment
    }
}