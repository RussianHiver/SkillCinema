package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.FragmentStaffPhotoFullBinding


class StaffPhotoFullFragment : Fragment() {

    private var _binding: FragmentStaffPhotoFullBinding? = null
    private val binding get() = _binding!!

    private val args: StaffPhotoFullFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffPhotoFullBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fullStaffPhoto.setOnClickListener {
            findNavController().popBackStack()
        }

        Glide.with(binding.fullStaffPhoto.context).load(args.photoUrl).into(binding.fullStaffPhoto)
    }

}