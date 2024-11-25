package com.example.skillcinema.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterForSearchResult(val onClickMoviePage:(Int?) -> Unit) :
    PagingDataAdapter<SearchResult.Item, SearchResultViewHolder>(DiffUtillCallback()) {
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
            onClickMoviePage(item?.kinopoiskId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }
}

class SearchResultViewHolder(val binding: MoviePreviewViewBinding) : ViewHolder(binding.root)

class DiffUtillCallback : DiffUtil.ItemCallback<SearchResult.Item>() {
    override fun areItemsTheSame(oldItem: SearchResult.Item, newItem: SearchResult.Item): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId


    override fun areContentsTheSame(
        oldItem: SearchResult.Item,
        newItem: SearchResult.Item
    ): Boolean = oldItem == newItem

}