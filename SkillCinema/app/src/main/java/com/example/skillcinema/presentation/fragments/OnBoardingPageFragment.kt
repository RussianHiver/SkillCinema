package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentOnboardingPageBinding


const val PAGE_NUMBER: String = "number"

private const val PAGE_ONE = 1
private const val PAGE_TWO = 2
private const val PAGE_THREE = 3

class BlankFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(PAGE_NUMBER) }?.apply {
            when (getInt(PAGE_NUMBER)) {
                PAGE_ONE -> {
                    binding.introductionPicture.setImageResource(R.drawable.firstscreen)
                    binding.explainText.text = getText(R.string.explanation_Page1)
                }

                PAGE_TWO -> {
                    binding.introductionPicture.setImageResource(R.drawable.secondscreen)
                    binding.explainText.text = getText(R.string.explanation_Page2)
                }

                PAGE_THREE -> {
                    binding.introductionPicture.setImageResource(R.drawable.thirdscreen)
                    binding.explainText.text = getText(R.string.explanation_Page3)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}