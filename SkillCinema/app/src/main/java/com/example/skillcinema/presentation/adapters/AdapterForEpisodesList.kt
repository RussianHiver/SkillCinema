package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.data.SeasonsAndEpisodesDTO
import com.example.skillcinema.databinding.EpisodeItemBinding

class AdapterForEpisodesList: ListAdapter<SeasonsAndEpisodesDTO.Item.Episode, EpisodesViewHolder>(DiffUtilEpisodesCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            episodeDate.text = item.releaseDate ?: "Дата неизвестна"
            episodeName.text = item.nameRu ?: item.nameEn ?: "Неизвестное имя"
        }
    }
}

class EpisodesViewHolder(val binding: EpisodeItemBinding): ViewHolder(binding.root)

class DiffUtilEpisodesCallback : DiffUtil.ItemCallback<SeasonsAndEpisodesDTO.Item.Episode>() {
    override fun areItemsTheSame(
        oldItem: SeasonsAndEpisodesDTO.Item.Episode,
        newItem: SeasonsAndEpisodesDTO.Item.Episode
    ): Boolean {
        return oldItem.nameRu == newItem.nameRu || oldItem.nameEn == newItem.nameEn
    }

    override fun areContentsTheSame(
        oldItem: SeasonsAndEpisodesDTO.Item.Episode,
        newItem: SeasonsAndEpisodesDTO.Item.Episode
    ): Boolean {
        return oldItem == newItem
    }

}