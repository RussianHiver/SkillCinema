package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.databinding.CountriesOrGenresFilterBinding

class AdapterForFilters(
    val isCountries: Boolean,
    val filters: GenresAndCountriesDTO,
    val onClick: (String) -> Unit
) :
    RecyclerView.Adapter<CountriesFilterViewHolder>() {

    private var countries: List<GenresAndCountriesDTO.Country> = emptyList()
    private var genres: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesFilterViewHolder {
        val binding = CountriesOrGenresFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountriesFilterViewHolder(binding)
    }

    override fun getItemCount(): Int = if (isCountries) countries.size else genres.size

    override fun onBindViewHolder(holder: CountriesFilterViewHolder, position: Int) {
        if (isCountries) {
            val item = countries.getOrNull(position)
            holder.binding.filterChoice.text = item?.country
            holder.binding.root.setOnClickListener {
                item?.let {
                    onClick(it.country)
                }
            }
        } else {
            val item = genres.getOrNull(position)
            item?.let {
                holder.binding.filterChoice.text = it
            }

            holder.binding.root.setOnClickListener {
                item?.let {
                    onClick(it)
                }
            }
        }



    }

    init {
        if (isCountries) {
            countries = filters.countries
        } else {
            genres =
                filters.genres.filter { checkedGenre -> checkedGenre.genre.isNotBlank() }.map {
                    it.genre.replaceFirst(it.genre[0], it.genre[0].uppercaseChar())
                }
        }
    }
}

class CountriesFilterViewHolder(val binding: CountriesOrGenresFilterBinding) :
    ViewHolder(binding.root)