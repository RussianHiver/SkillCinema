package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentBottomSheetBinding
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.CollectionWithFilms
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.BottomSheetCollectionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch


class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!


    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private var id = 0

    private val collectionButtonSheetAdapter by lazy {
        BottomSheetCollectionAdapter(id) { state, element ->
            onClickCheckbox(element, id, state)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        id = arguments?.getInt("ID") ?: 0
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exit.setOnClickListener {
            this.dismiss()
        }

        dataViewModel.movieData.observe(viewLifecycleOwner) { movie ->
            id = movie.kinopoiskId
            binding.recyclerAddCollection.adapter = collectionButtonSheetAdapter
            with(binding) {
                Glide.with(imageSheet.context).load(movie.posterUrl).into(imageSheet)
                textNameSheet.text = movie.nameRu ?: movie.nameEn ?: "Неизвестный"
                textYearSheet.text = movie.year?.toString() ?: "Без даты"
                textGenreSheet.text = movie.genres.first().genre
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allCollection.collect { item ->
                collectionButtonSheetAdapter.submitList(item)
            }
        }

        binding.addPlus.setOnClickListener {
            context?.let {
                val dialog = getDialog(it, activity, viewModel)
                dialog.show()
            }
        }

    }

    private fun onClickCheckbox(item: CollectionWithFilms, id: Int, state: String) {
        if (state == LIST_STATE_ADD) {
            item.collection.collectionId?.let {
                viewModel.insertFilmForCollection(
                    CollectionListFilms(it, id)
                )
            }
        } else if (state == LIST_STATE_REMOVE) {
            item.collection.collectionId?.let {
                viewModel.deleteFilmForCollection(
                    CollectionListFilms(it, id)
                )
            }
        }
    }

    private fun getDialog(
        context: Context,
        activity: FragmentActivity?,
        viewModel: MainViewModel
    ): androidx.appcompat.app.AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.CustomMaterialAlertDialog)
        val view = activity?.layoutInflater?.inflate(R.layout.dialog_add_collection, null)
        val editText = view?.findViewById<TextInputEditText>(R.id.edit_text)
        return with(builder) {
            setTitle(R.string.add_name_collection)
            setView(view)
            setPositiveButton(R.string.done) { _, _ ->
                viewModel.insertCollection(CollectionEntity(null, editText?.text.toString()))
            }
            create()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {
        private const val LIST_STATE_ADD = "LIST_STATE_ADD"
        private const val LIST_STATE_REMOVE = "LIST_STATE_REMOVE"
    }

}