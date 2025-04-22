package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.MoviePreviewViewBinding
import com.example.skillcinema.databinding.WatchedMovieClearButtonViewBinding
import com.example.skillcinema.entity.FilmEntity

class WatchedMoviesAdapter(
    private val clickDeleteAllFilms: (List<FilmEntity>) -> Unit,
    private val onClick: (Int?) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var data: List<FilmEntity> = emptyList()
    private var dataSize = 0

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) 0 else 1
    }

    fun setData(data: List<FilmEntity>){
        this.data = data
        dataSize = if (data.size > 20) 20 else data.size
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (data.isEmpty()) 0 else dataSize + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            1 -> {
                val movieBinding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WatchedMovieViewHolder(movieBinding)
            }
            else -> {
                val buttonBinding = WatchedMovieClearButtonViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DeleteButtonViewHolder(buttonBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (holder is WatchedMovieViewHolder) {
            val dataItem = data[position]
            with(holder.binding){
                movieTitle.text = dataItem.nameRu
                movieGenre.text = dataItem.genre
                dataItem.let {
                    Glide.with(poster.context)
                        .load(it.posterUrl)
                        .into(poster)
                }
                dataItem.rating?.let {
                    with(movieRating) {
                        isVisible = true
                        text = String.format("%.1f", it)
                    }
                }

                root.setOnClickListener {
                    onClick(dataItem.id)
                }
            }
        }

        if (holder is DeleteButtonViewHolder) {
            holder.binding.root.setOnClickListener {
                clickDeleteAllFilms(data)
            }
        }

    }

}
class WatchedMovieViewHolder (val binding: MoviePreviewViewBinding) : ViewHolder(binding.root)

class DeleteButtonViewHolder (val binding: WatchedMovieClearButtonViewBinding) : ViewHolder(binding.root)

