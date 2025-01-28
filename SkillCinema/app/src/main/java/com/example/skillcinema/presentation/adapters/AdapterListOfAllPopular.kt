package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterListOfAllPopular(val onClickMoviePage: (Int?) -> Unit) :
    PagingDataAdapter<PopularDTO.Item, PopularViewHolder>(
        DiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding =
            MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
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

class PopularViewHolder(val binding: MoviePreviewViewBinding) : ViewHolder(binding.root)
class DiffUtilCallback : DiffUtil.ItemCallback<PopularDTO.Item>() {
    override fun areItemsTheSame(
        oldItem: PopularDTO.Item, newItem: PopularDTO.Item
    ): Boolean = oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: PopularDTO.Item,
        newItem: PopularDTO.Item
    ): Boolean = oldItem == newItem

}