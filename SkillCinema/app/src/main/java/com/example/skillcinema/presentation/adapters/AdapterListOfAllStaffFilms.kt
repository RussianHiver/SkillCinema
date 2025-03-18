package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterListOfAllStaffFilms(
    val onClickMoviePage: (Int?) -> Unit
) :
    ListAdapter<MovieDataDTO, AllStaffFilmsViewHolder>(DiffUtilFilmographyMovieCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllStaffFilmsViewHolder {
        val binding =
            MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllStaffFilmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllStaffFilmsViewHolder, position: Int) {

        val item = getItem(position)

        with(holder.binding) {
            movieTitle.text = item?.nameRu
            if (item?.genres?.isNotEmpty() == true) {
                movieGenre.text = item.genres.first().genre
            }
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

        holder.binding.root.setOnClickListener {
            onClickMoviePage(item?.kinopoiskId)
        }
    }
}

class AllStaffFilmsViewHolder(val binding: MoviePreviewViewBinding) :
    RecyclerView.ViewHolder(binding.root)

class DiffUtilFilmographyMovieCallback : DiffUtil.ItemCallback<MovieDataDTO>() {
    override fun areItemsTheSame(oldItem: MovieDataDTO, newItem: MovieDataDTO): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(oldItem: MovieDataDTO, newItem: MovieDataDTO): Boolean {
        return oldItem == newItem
    }


}