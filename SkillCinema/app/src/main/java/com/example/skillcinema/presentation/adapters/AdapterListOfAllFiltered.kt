package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterListOfAllFiltered(val onClickMoviePage:(Int?) -> Unit): PagingDataAdapter<FilteredDTO.Item, FilteredViewHolder> (
    DiffUtilCallbackFiltered()
) {
    override fun onBindViewHolder(holder: FilteredViewHolder, position: Int) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilteredViewHolder(binding)
    }
}


class FilteredViewHolder(val binding: MoviePreviewViewBinding): ViewHolder(binding.root)
class DiffUtilCallbackFiltered: DiffUtil.ItemCallback<FilteredDTO.Item>() {
    override fun areItemsTheSame(oldItem: FilteredDTO.Item, newItem: FilteredDTO.Item): Boolean
    = oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: FilteredDTO.Item, newItem: FilteredDTO.Item): Boolean
    = oldItem == newItem

}