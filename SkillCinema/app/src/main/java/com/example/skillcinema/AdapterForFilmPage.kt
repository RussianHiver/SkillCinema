package com.example.skillcinema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.data.Staff
import com.example.skillcinema.databinding.FilmStaffBinding

class AdapterForFilmPage(val staff: List<Staff.StaffItem>) : Adapter<FilmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = FilmStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun getItemCount(): Int = staff.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {

        val item = staff.getOrNull(position)
        with(holder.binding) {
            Glide.with(poster.context)
                .load(item?.posterUrl)
                .into(poster)
            name.text = item?.nameRu
            occupation.text = item?.professionText
        }
    }
}

class FilmViewHolder(val binding: FilmStaffBinding) : ViewHolder(binding.root)