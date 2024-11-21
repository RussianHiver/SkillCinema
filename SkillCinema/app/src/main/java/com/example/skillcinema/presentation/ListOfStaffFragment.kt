package com.example.skillcinema.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.skillcinema.data.AdapterForFilmPage
import com.example.skillcinema.data.RetrofitInstance
import com.example.skillcinema.databinding.FragmentListOfStaffBinding
import kotlinx.coroutines.launch


class ListOfStaffFragment : Fragment() {

    private var _binding: FragmentListOfStaffBinding? = null
    private val binding get() = _binding!!

    private val args: ListOfStaffFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId = args.FilmId

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val staff = RetrofitInstance.searchDataApi.getFilmStaff(filmId)
                binding.recyclerStaff.adapter = AdapterForFilmPage(
                    staff,
                    args.TypeOfStaff,
                    true
                ) { id -> openStaffMemberPage(id) }
            }
        }


    }

    fun openStaffMemberPage(id: Int) {
        val action = ListOfStaffFragmentDirections.actionListOfStaffFragmentToActorPageFragment(id)
        findNavController().navigate(action)
    }

}