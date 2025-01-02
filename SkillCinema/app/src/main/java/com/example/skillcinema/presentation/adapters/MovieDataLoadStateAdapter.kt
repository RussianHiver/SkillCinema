package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.databinding.LoadItemStateBinding

class MovieDataLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
        holder.binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadItemStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }
}

class LoadStateViewHolder(val binding: LoadItemStateBinding): ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error){
            binding.errorText.text = loadState.error.localizedMessage
        }

        binding.stateProgress.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorText.isVisible = loadState !is LoadState.Loading

    }

}