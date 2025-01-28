package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.PremieresDTO
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class AdapterListOfAllPremiers(
    val premiers: List<PremieresDTO.Item>,
    val onClickMoviePage:(Int?) -> Unit
): RecyclerView.Adapter<PremiersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremiersViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PremiersViewHolder(binding)
    }

    override fun getItemCount(): Int = premiers.size

    override fun onBindViewHolder(holder: PremiersViewHolder, position: Int) {
        val item = premiers.getOrNull(position)
        with(holder.binding) {
            movieTitle.text = item?.nameRu
            if (item?.genres?.isNotEmpty() == true) {
                movieGenre.text = item.genres.first().genre
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

class PremiersViewHolder(val binding: MoviePreviewViewBinding): RecyclerView.ViewHolder(binding.root)