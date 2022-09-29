package com.example.gratefulnote.mainfragment

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion

@BindingAdapter("tanggal")
fun TextView.setTanggal(item : PositiveEmotion){
    val dateString = "${item.day}/${item.month}/${item.year}"
    text = dateString
}

@BindingAdapter("judul")
fun TextView.setPenyebab(what : String){
    text = what
}

@BindingAdapter("tipe")
fun TextView.setTipe(type : String){
    text = type
}

@BindingAdapter("starVisibility")
fun AppCompatImageView.setVisibility(isFavorite : Boolean){
    setImageResource(
        if (isFavorite) R.drawable.ic_baseline_yellow_star
        else R.drawable.ic_outline_star_border
    )
}