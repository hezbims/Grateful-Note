package com.example.gratefulnote.mainfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.ViewHolderMainBinding

class MainRecyclerViewAdapter(private val clickListener: PositiveAdapterClickListener) :
    ListAdapter<PositiveEmotion , MainRecyclerViewAdapter.ViewHolder>(DiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ViewHolderMainBinding
            .inflate(LayoutInflater
                .from(parent.context) , parent , false),
        clickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        private val binding : ViewHolderMainBinding,
        private val clickListener: PositiveAdapterClickListener
        ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : PositiveEmotion){
            binding.positiveEmotion = item
            binding.deletePositiveEmotion.setOnClickListener{ clickListener.onDelete(item.id) }
            binding.editPositiveEmotion.setOnClickListener{ clickListener.onEdit(item) }
            binding.favoriteSymbol.setOnClickListener {

                binding.favoriteSymbol.apply {
                    val nextDrawableId =
                        if (tag == R.drawable.ic_baseline_yellow_star)
                            R.drawable.ic_outline_star_border
                        else
                            R.drawable.ic_baseline_yellow_star
                    clickListener.onFavoriteUpdate(item.copy(
                        isFavorite = nextDrawableId == R.drawable.ic_baseline_yellow_star
                    ))
                    setImageResource(nextDrawableId)
                    tag = nextDrawableId
                }
            }
            binding.executePendingBindings()
        }

    }
}

class DiffCallBack : DiffUtil.ItemCallback<PositiveEmotion>(){
    override fun areItemsTheSame(oldItem: PositiveEmotion, newItem: PositiveEmotion) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PositiveEmotion, newItem: PositiveEmotion) =
        oldItem == newItem
}

class PositiveAdapterClickListener(
    private val delete : (itemId : Long) -> Unit,
    private val edit : (currentPositiveEmotion : PositiveEmotion) -> Unit,
    private val favoriteUpdate : (positiveEmotion : PositiveEmotion) -> Unit
){

    fun onDelete(itemId : Long) = delete(itemId)
    fun onEdit(currentPositiveEmotion: PositiveEmotion) = edit(currentPositiveEmotion)
    fun onFavoriteUpdate(positiveEmotion: PositiveEmotion) = favoriteUpdate(positiveEmotion)
}