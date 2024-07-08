package com.example.skillcinema

import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Filtered
import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.Premieres
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class Adapter() : RecyclerView.Adapter<MovieViewHolder>() {

    var listPremier: List<Premieres.Item> = emptyList()
    var listPopular: List<Popular.Item> = emptyList()
    var listFiltered: List<Filtered.Item> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int  = 9

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        if (position == 8) {
            holder.binding.poster.visibility = View.GONE
            holder.binding.movieTitle.visibility = View.GONE
            holder.binding.movieGenre.visibility = View.GONE
        } else {
            when {
                listPremier.isNotEmpty() -> {
                    val item = listPremier.getOrNull(position)
                    with(holder.binding) {
                        movieTitle.text = item?.nameRu
                        movieGenre.text = item?.genres?.first()?.genre
                        item?.let {
                            Glide.with(poster.context)
                                .load(it.posterUrlPreview)
                                .into(poster)
                        }
                    }
                }
                listPopular.isNotEmpty() -> {
                    val item = listPopular.getOrNull(position)
                    with(holder.binding) {
                        movieTitle.text = item?.nameRu
                        movieGenre.text = item?.genres?.first()?.genre
                        item?.let {
                            Glide.with(poster.context)
                                .load(it.posterUrlPreview)
                                .into(poster)
                        }
                    }
                }

                listFiltered.isNotEmpty() -> {
                    val item = listFiltered.getOrNull(position)
                    with(holder.binding) {
                        movieTitle.text = item?.nameRu
                        movieGenre.text = item?.genres?.first()?.genre
                        item?.let {
                            Glide.with(poster.context)
                                .load(it.posterUrlPreview)
                                .into(poster)
                        }
                    }
                }
            }
        }
    }


    fun addPremiers(premieres: List<Premieres.Item>) {
       listPremier = premieres
    }

    fun addPopular(popular: List<Popular.Item>) {
        listPopular = popular
    }

    fun addFiltered(filtered: List<Filtered.Item>) {
        listFiltered = filtered
    }

}

class MovieViewHolder (val binding: MoviePreviewViewBinding) : RecyclerView.ViewHolder(binding.root)