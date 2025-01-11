package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.SimilarsDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterForSimilars(val similars: SimilarsDTO, val allFilms: Boolean): Adapter<SimilarsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarsViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return when {
            allFilms -> similars.total
            similars.total <= 4 -> similars.total
            else -> 4
        }
    }

    override fun onBindViewHolder(holder: SimilarsViewHolder, position: Int) {
        val item = similars.items.getOrNull(position)
        with(holder.binding) {
            movieTitle.text = item?.nameRu
            movieGenre.visibility = View.GONE
            item?.let {
                Glide.with(poster.context).load(it.posterUrlPreview).into(poster)
            }
        }
    }
}

class SimilarsViewHolder(val binding: MoviePreviewViewBinding): ViewHolder(binding.root)