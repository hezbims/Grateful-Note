package com.example.gratefulnote.mainfragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.PositiveEmotionListBinding

class PositiveAdapter(private val viewModel : MainViewModel) : RecyclerView.Adapter<PositiveAdapter.ViewHolder>(){
    var data = listOf<PositiveEmotion>()
    @SuppressLint("NotifyDataSetChanged")
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        PositiveEmotionListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    inner class ViewHolder(private val binding : PositiveEmotionListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : PositiveEmotion){
            binding.positiveEmotion = item
            binding.deletePositiveEmotion.setOnClickListener{
                this@PositiveAdapter.viewModel.delete(item.id)
            }
            binding.executePendingBindings()
        }

    }
}