package com.example.gratefulnote.mainMenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.R
import com.example.gratefulnote.database.Diary
import com.example.gratefulnote.databinding.ViewHolderMainMenuListItemBinding

class MainRecyclerViewAdapter(private val clickListener: PositiveAdapterClickListener) :
    ListAdapter<Diary , MainRecyclerViewAdapter.ViewHolder>(DiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ViewHolderMainMenuListItemBinding
            .inflate(LayoutInflater
                .from(parent.context) , parent , false),
        clickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(
        private val binding : ViewHolderMainMenuListItemBinding,
        private val clickListener: PositiveAdapterClickListener
        ) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Diary){
            binding.diary = item
            binding.deleteDiaryIcon.setOnClickListener{ clickListener.onDelete(item.id) }
            binding.editDiaryIcon.setOnClickListener{ clickListener.onEdit(item) }
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

class DiffCallBack : DiffUtil.ItemCallback<Diary>(){
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary) =
        oldItem == newItem
}

class PositiveAdapterClickListener(
    private val delete : (itemId : Long) -> Unit,
    private val edit : (currentDiary : Diary) -> Unit,
    private val favoriteUpdate : (diary : Diary) -> Unit
){

    fun onDelete(itemId : Long) = delete(itemId)
    fun onEdit(currentDiary: Diary) = edit(currentDiary)
    fun onFavoriteUpdate(diary: Diary) = favoriteUpdate(diary)
}