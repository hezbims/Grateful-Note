package com.example.gratefulnote.mainfragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.PositiveEmotionListBinding

class PositiveAdapter : RecyclerView.Adapter<PositiveAdapter.ViewHolder>(){
    var data = listOf<PositiveEmotion>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])


    class ViewHolder private constructor(private val binding : PositiveEmotionListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(positiveEmotion : PositiveEmotion){
            binding.positiveEmotion = positiveEmotion
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent : ViewGroup) : ViewHolder{
                val newBinding = PositiveEmotionListBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
                return ViewHolder(newBinding)
            }
        }
    }
}