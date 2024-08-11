package com.example.skillcinema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Filtered
import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.Premieres
import com.example.skillcinema.databinding.MoviePreviewViewBinding
import com.example.skillcinema.databinding.ShowEverythingButtonViewBinding

class AdapterForHomePage(
    val onClick:() -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var listPremier: List<Premieres.Item> = emptyList()
    private var listPopular: List<Popular.Item> = emptyList()
    private var listFiltered: List<Filtered.Item> = emptyList()

    private var numberOfItems = 15
    fun checkTheNumberOfItems(number: Int) {
        numberOfItems = if (number < 20) number else 21
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 20) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            1 -> {
                val movieBinding = MoviePreviewViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(movieBinding)
            }
            else -> {
                val buttonBinding = ShowEverythingButtonViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ButtonViewHolder(buttonBinding)
            }
        }
    }

    override fun getItemCount(): Int  = numberOfItems

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder is MovieViewHolder) {
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

        if (holder is ButtonViewHolder) {
            holder.binding.root.setOnClickListener {
                onClick()
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

class MovieViewHolder (val binding: MoviePreviewViewBinding) : ViewHolder(binding.root)

class ButtonViewHolder (val binding: ShowEverythingButtonViewBinding) : ViewHolder(binding.root)