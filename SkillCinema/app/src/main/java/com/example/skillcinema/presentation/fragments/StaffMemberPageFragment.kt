package com.example.skillcinema.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.skillcinema.data.App
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.databinding.FragmentActorPageBinding
import com.example.skillcinema.di.DaggerAppComponent
import com.example.skillcinema.presentation.MainViewModel
import kotlinx.coroutines.launch

class StaffMemberPageFragment : Fragment() {

    private var _binding: FragmentActorPageBinding? = null
    private val binding get() = _binding!!

    private val args: StaffMemberPageFragmentArgs by navArgs()

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private lateinit var staffMember: StaffMemberDTO
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.headerBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                staffMember = viewModel.giveIndividualStaffMember(id = args.PersonId)
                with(binding) {
                    Glide.with(actorPhoto.context).load(staffMember.posterUrl).into(actorPhoto)
                    name.text = staffMember.nameRu ?: staffMember.nameEn ?: "Без названия"
                    occupation.text = staffMember.profession
                    amountOfFilmsInFilmography.text = getFilmCountString(staffMember.films.size)
                }

                binding.filmographyButton.setOnClickListener {
                    val action =
                        StaffMemberPageFragmentDirections.actionActorPageFragmentToStaffFilmographyFragment(
                            staffMember.staffId
                        )
                    findNavController().navigate(action)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getFilmCountString(films: Int): String {
        return when (films) {
            1 -> "$films фильм"
            in (2..4) -> "$films фильма"
            else -> "$films фильмов"
        }
    }

}