package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterForStaffBestFilms(val list: MutableList<MovieDataDTO>): RecyclerView.Adapter<StaffBestFilmsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffBestFilmsViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffBestFilmsViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: StaffBestFilmsViewHolder, position: Int) {
        val item = list.getOrNull(position)
        with(holder.binding) {
            movieTitle.text = item?.nameRu
            movieGenre.text = item?.genres?.first()?.genre
            item?.ratingKinopoisk?.let {
                with(movieRating) {
                    isVisible = true
                    text = String.format("%.1f", it)
                }
            }
            item?.let {
                Glide.with(poster.context)
                    .load(it.posterUrlPreview)
                    .into(poster)
            }
        }
    }
}

class StaffBestFilmsViewHolder(val binding: MoviePreviewViewBinding): ViewHolder(binding.root)