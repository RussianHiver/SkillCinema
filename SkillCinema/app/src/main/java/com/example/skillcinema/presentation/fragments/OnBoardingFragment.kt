package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentOnBoardingBinding
import com.example.skillcinema.presentation.adapters.FragmentAdapter

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FragmentAdapter
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FragmentAdapter(requireActivity())
        viewPager = binding.pager
        viewPager.adapter = adapter

        binding.circleIndicator.setViewPager(viewPager)

        binding.skipButton.setOnClickListener {
            with(binding) {
                progressBarBeforeHomepage.isVisible = true
                pager.isVisible = false
                circleIndicator.isVisible = false
                introductionPicture.isVisible = true
            }
            findNavController().navigate(R.id.action_onBoardingFragment_to_homepageFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}