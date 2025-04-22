package com.example.skillcinema.presentation.adapters

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinema.databinding.AddCollectionItemBinding
import com.example.skillcinema.entity.CollectionWithFilms

class BottomSheetCollectionAdapter(private var movie: Int,
    private var onClick: (state: String, CollectionWithFilms) -> Unit
) : ListAdapter<CollectionWithFilms, CollectionItemViewHolder>(DiffUtilCollectionItemCallback()) {

    private val checkedState = SparseBooleanArray()
    private var state = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionItemViewHolder {
        val binding = AddCollectionItemBinding.inflate(LayoutInflater.from(parent.context))
        return CollectionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionItemViewHolder, position: Int) {
        val dataItem = getItem(position)
        dataItem.films?.forEach {
            if (movie == it.id) checkedState.put(position, true)
        }
        with(holder.binding){
            addNewCollection.text = dataItem?.collection?.nameCollection ?:""
            count.text = dataItem?.films?.size.toString()
            checkboxSheet.isChecked = checkedState.get(position, false)

            checkboxSheet.setOnClickListener{
                if (!checkedState.get(position,false)){
                    checkboxSheet.isChecked = true
                    checkedState.put(position, true)
                    state = LIST_STATE_ADD
                }else{
                    state = LIST_STATE_REMOVE
                    checkboxSheet.isChecked = false
                    checkedState.put(position, false)
                }
                dataItem?.let { onClick(state, dataItem) }
            }
        }
    }
    companion object{
        private const val LIST_STATE_ADD = "LIST_STATE_ADD"
        private const val LIST_STATE_REMOVE = "LIST_STATE_REMOVE"
    }

}
class CollectionItemViewHolder(val binding: AddCollectionItemBinding) : RecyclerView.ViewHolder(binding.root)

class DiffUtilCollectionItemCallback : DiffUtil.ItemCallback<CollectionWithFilms>() {
    override fun areItemsTheSame(
        oldItem: CollectionWithFilms,
        newItem: CollectionWithFilms
    ): Boolean {
        return oldItem.collection.collectionId == newItem.collection.collectionId
    }

    override fun areContentsTheSame(
        oldItem: CollectionWithFilms,
        newItem: CollectionWithFilms
    ): Boolean {
        return oldItem == newItem
    }

}