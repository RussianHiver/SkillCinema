package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.SearchResultDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterForSearchResult(val onClick: (Int?) -> Unit): PagingDataAdapter<FilteredDTO.Item, SearchResultViewHolder>(
    DiffUtillCallbackSearchResult()
) {
    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            movieTitle.text = item?.nameRu
            movieGenre.text = item?.genres?.first()?.genre
            item?.let {
                Glide.with(poster.context)
                    .load(it.posterUrlPreview)
                    .into(poster)
            }
        }

        holder.binding.root.setOnClickListener {
            onClick(item?.kinopoiskId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }
}

class DiffUtillCallbackSearchResult: DiffUtil.ItemCallback<FilteredDTO.Item>() {
    override fun areItemsTheSame(oldItem: FilteredDTO.Item, newItem: FilteredDTO.Item): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(
        oldItem: FilteredDTO.Item,
        newItem: FilteredDTO.Item
    ): Boolean {
        return oldItem == newItem
    }

}
class SearchResultViewHolder(val binding: MoviePreviewViewBinding): ViewHolder(binding.root)