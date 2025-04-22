package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.MoviePreviewViewBinding
import com.example.skillcinema.entity.FilmEntity

class AdapterForListOfAllWatchedFilms(val onClickMoviePage: (Int?) -> Unit) :
    ListAdapter<FilmEntity, WatchedFilmsListViewHolder>(
        WatchedFilmsDiffUtilCallback()
    ) {
    override fun onBindViewHolder(holder: WatchedFilmsListViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            movieTitle.text = item?.nameRu ?: "Неизвестный"
            movieGenre.text = item?.genre ?: "Неизвестный"
            item.let {
                Glide.with(poster.context)
                    .load(it?.posterUrl)
                    .into(poster)
            }
            item?.rating?.let {
                with(movieRating) {
                    isVisible = true
                    text = String.format("%.1f", it)
                }
            }

            root.setOnClickListener {
                onClickMoviePage(item?.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedFilmsListViewHolder {
        val binding =
            MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchedFilmsListViewHolder(binding)
    }

}

class WatchedFilmsListViewHolder(val binding: MoviePreviewViewBinding): ViewHolder(binding.root)

class WatchedFilmsDiffUtilCallback: DiffUtil.ItemCallback<FilmEntity>() {
    override fun areItemsTheSame(oldItem: FilmEntity, newItem: FilmEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FilmEntity, newItem: FilmEntity): Boolean {
        return oldItem == newItem
    }

}