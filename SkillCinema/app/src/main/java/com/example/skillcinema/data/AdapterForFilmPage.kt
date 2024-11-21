package com.example.skillcinema.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinema.databinding.FilmStaffBinding

class AdapterForFilmPage(
    staff: List<StaffItem>,
    areActors: Boolean,
    val fullList: Boolean,
    val onClick: (Int) -> Unit
) : Adapter<FilmViewHolder>() {

    val newStaff: List<StaffItem> = if (areActors) {
        staff.filter { it.professionKey == "ACTOR" }
    } else {
        staff.filter { it.professionKey != "ACTOR" }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val binding = FilmStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return when (fullList) {
            true -> newStaff.size
            else -> if (newStaff.size > 6) 6 else newStaff.size
        }
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {

        val item = newStaff.getOrNull(position)

        with(holder.binding) {
            Glide.with(poster.context)
                .load(item?.posterUrl)
                .into(poster)
            name.text = item?.nameRu
            occupation.text = item?.professionText
        }

        holder.binding.root.setOnClickListener {
            item?.let {
                onClick(it.staffId)
            }
        }

    }
}

class FilmViewHolder(val binding: FilmStaffBinding) : ViewHolder(binding.root)