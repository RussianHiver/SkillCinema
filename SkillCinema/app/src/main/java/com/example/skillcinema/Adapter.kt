package com.example.skillcinema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.Premieres
import com.example.skillcinema.databinding.MoviePreviewViewBinding

class Adapter(
    private var premiers: List<Premieres.Item>?,
    private var popular: List<Popular.Item>?
) : RecyclerView.Adapter<MovieViewHolder>() {

    private lateinit var list: List<Any>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int  = 8

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        when {
            premiers != null -> list = premiers as List<Premieres.Item>
        }

        val item = premiers?.getOrNull(position)
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

class MovieViewHolder (val binding: MoviePreviewViewBinding) : RecyclerView.ViewHolder(binding.root)