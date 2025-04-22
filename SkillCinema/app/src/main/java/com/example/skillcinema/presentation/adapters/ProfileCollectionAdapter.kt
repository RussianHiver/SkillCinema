package com.example.skillcinema.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.R
import com.example.skillcinema.databinding.CollectionItemBinding
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionWithFilms
import com.example.skillcinema.entity.FilmEntity

class ProfileCollectionAdapter(
    private val clickDelete: (CollectionEntity) -> Unit,
    private val clickAllCollection: (List<FilmEntity>?) -> Unit
) : RecyclerView.Adapter<ViewHolderCollection>() {

    private var data: List<CollectionWithFilms> = emptyList()

    fun setData(data: List<CollectionWithFilms>){
        this.data = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCollection {
        val binding = CollectionItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolderCollection(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderCollection, position: Int) {
        val dataItem = data[position]
        with(holder.binding) {
            textCollection.text = dataItem.collection.nameCollection
            countCollection.text = dataItem.films?.size.toString()
            if (position == 0){
                imageCollection.setImageResource(R.drawable.favorite_black)
                delete.isVisible = false
            }
            if (position == 1){
                imageCollection.setImageResource(R.drawable.bookmark_black)
                delete.isVisible = false
            }

            delete.setOnClickListener {
                clickDelete(dataItem.collection)
            }
            containerCollection.setOnClickListener{
                clickAllCollection(dataItem.films)
            }

        }

    }
    override fun getItemCount(): Int {
        return data.size
    }
}
class ViewHolderCollection(val binding: CollectionItemBinding) : RecyclerView.ViewHolder(binding.root)