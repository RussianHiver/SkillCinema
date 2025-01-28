package com.example.skillcinema.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.MoviePreviewViewBinding
import com.example.skillcinema.databinding.ShowEverythingButtonViewBinding

class AdapterForStaffPage(
    val onClickMoviePage:(Int?) -> Unit,
    val onClickAllButton:(String) -> Unit
): RecyclerView.Adapter<StaffMovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffMovieViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: StaffMovieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class StaffMovieViewHolder (val binding: MoviePreviewViewBinding) : ViewHolder(binding.root)

class StaffButtonViewHolder (val binding: ShowEverythingButtonViewBinding) : ViewHolder(binding.root)