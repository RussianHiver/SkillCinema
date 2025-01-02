package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.databinding.ImageForGalleryBinding

class AdapterForGallery(val images: List<GalleryDTO.Item>) : Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding =
            ImageForGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {

        val item = images.getOrNull(position)

        Glide.with(holder.binding.movieImage.context).load(item?.imageUrl)
            .into(holder.binding.movieImage)
    }
}

class GalleryViewHolder(val binding: ImageForGalleryBinding) : ViewHolder(binding.root)