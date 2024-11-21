package com.example.skillcinema.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.ImageForGalleryBinding

class AdapterForFullGallery : PagingDataAdapter<Gallery.Item, FullGalleryViewHolder> (DiffUtilCallbackGallery()) {
    override fun onBindViewHolder(holder: FullGalleryViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            item?.let {
                Glide.with(movieImage.context).load(it.imageUrl).into(movieImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullGalleryViewHolder {
        val binding = ImageForGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FullGalleryViewHolder(binding)
    }
}

class FullGalleryViewHolder(val binding: ImageForGalleryBinding) : ViewHolder(binding.root)

class DiffUtilCallbackGallery : DiffUtil.ItemCallback<Gallery.Item>() {
    override fun areItemsTheSame(oldItem: Gallery.Item, newItem: Gallery.Item): Boolean =
        oldItem.imageUrl == newItem.imageUrl


    override fun areContentsTheSame(oldItem: Gallery.Item, newItem: Gallery.Item): Boolean =
        oldItem == newItem

}