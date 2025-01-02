package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.MoviePreviewViewBinding
import com.example.skillcinema.databinding.ShowEverythingButtonViewBinding

class AdapterForProfilePage(
    val onClickMoviePage:(Int?) -> Unit,
    val onClickAllButton:(String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {


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

        }

        /*if (holder is ClearButtonViewHolder) {
            holder.binding.root.setOnClickListener {
                onClickAllButton()
            }
        }*/
    }

}

class ClearButtonViewHolder (val binding: ShowEverythingButtonViewBinding) : ViewHolder(binding.root)