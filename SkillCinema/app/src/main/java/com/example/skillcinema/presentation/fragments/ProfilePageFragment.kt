package com.example.skillcinema.presentation.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.data.App
import com.example.skillcinema.databinding.FragmentProfilePageBinding
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.FilmEntity
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.adapters.ProfileCollectionAdapter
import com.example.skillcinema.presentation.adapters.WatchedMoviesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class ProfilePageFragment : Fragment() {

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        (activity?.application as App).component.mainViewModelFactory()
    }

    private val dataViewModel: DataViewModel by activityViewModels {
        (activity?.application as App).component.dataViewModelFactory()
    }

    private val watchedMoviesAdapter =
        WatchedMoviesAdapter({ filmList -> deleteMovieList(filmList) }, { id -> openMoviePage(id) })

    private val profileCollectionAdapter =
        ProfileCollectionAdapter({ collection -> onClickDelete(collection) },
            { list -> onClickOpenAll(list) })

    private val interestingMoviesAdapter =
        WatchedMoviesAdapter(
            { filmList -> deleteInterestingMovieList(filmList) },
            { id -> openMoviePage(id) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            if (checkForInternet(context)) {
                var interestingMovieList = emptyList<FilmEntity>()

                binding.recyclerSeen.adapter = watchedMoviesAdapter
                binding.recyclerCollection.adapter = profileCollectionAdapter
                binding.recyclerInterested.adapter = interestingMoviesAdapter

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewModel.allCollection.collect { item ->
                                val numberOfFilms = item.elementAtOrNull(2)?.films?.size
                                binding.allButtonSeen.text = "${numberOfFilms.toString()} >"

                                item.elementAtOrNull(2)?.films?.let { list ->
                                    watchedMoviesAdapter.setData(list)
                                    dataViewModel.setWatchedFilmsList(list)
                                }
                            }
                        }

                        binding.createPlus.setOnClickListener {
                            context.let {
                                val dialog = getDialog(it, activity, viewModel)
                                dialog.show()
                            }
                        }

                        launch {
                            viewModel.allCollection.collect { item ->
                                val noWatchedList = item.filter { it.collection.collectionId != 3 }
                                profileCollectionAdapter.setData(noWatchedList)
                            }
                        }

                        launch {
                            viewModel.allCollection.collect { item ->
                                val numberOfFilms = item.elementAtOrNull(1)?.films?.size
                                binding.allButtonInterested.text = "${numberOfFilms.toString()} >"

                                item.elementAtOrNull(1)?.films?.let { list ->
                                    interestingMoviesAdapter.setData(list)
                                    interestingMovieList = list
                                }
                            }
                        }

                    }
                }

                binding.allButtonSeen.setOnClickListener {
                    findNavController().navigate(R.id.listOfWatchedFilmsFragment)
                }

                binding.allButtonInterested.setOnClickListener {
                    dataViewModel.setWatchedFilmsList(interestingMovieList)
                    findNavController().navigate(R.id.listOfWatchedFilmsFragment)
                }
            } else {
                BottomSheetErrorFragment().show(parentFragmentManager, "sheetError")
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openMoviePage(id: Int?) {
        id?.let {
            val action = ProfilePageFragmentDirections.actionProfilePageFragmentToFilmPage(it)
            findNavController().navigate(action)
        }
    }

    private fun deleteInterestingMovieList(filmList: List<FilmEntity>) {
        filmList.forEach { film ->
            viewModel.deleteFilmForCollection(CollectionListFilms(2, film.id))
        }
    }

    private fun deleteMovieList(filmList: List<FilmEntity>) {
        filmList.forEach { film ->
            viewModel.deleteFilmForCollection(CollectionListFilms(3, film.id))
        }
    }

    private fun onClickDelete(item: CollectionEntity) {
        viewModel.deleteCollection(item)
    }

    private fun onClickOpenAll(list: List<FilmEntity>?) {
        list?.let {
            dataViewModel.setWatchedFilmsList(it)
            val action =
                ProfilePageFragmentDirections.actionProfilePageFragmentToListOfWatchedFilmsFragment()
            findNavController().navigate(action)
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

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }
}