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
import com.bumptech.glide.Glide
import com.example.skillcinema.data.RetrofitInstance
import com.example.skillcinema.data.StaffMember
import com.example.skillcinema.databinding.FragmentActorPageBinding
import kotlinx.coroutines.launch

class StaffMemberPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null
    private val binding get() = _binding!!

    private val args: StaffMemberPageFragmentArgs by navArgs()

    private lateinit var staffMember: StaffMember
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                staffMember = RetrofitInstance.searchDataApi.getIndividualStaffMember(args.PersonId)
                with(binding) {
                    Glide.with(actorPhoto.context).load(staffMember.posterUrl).into(actorPhoto)
                    actorName.text = staffMember.nameRu ?: staffMember.nameEn ?: "Без названия"
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}