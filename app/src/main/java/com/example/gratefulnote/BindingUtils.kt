package com.example.gratefulnote

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.gratefulnote.database.PositiveEmotion

@BindingAdapter("tanggal")
fun TextView.setTanggal(item : PositiveEmotion){
    val dateString = "${item.day}/${item.month}/${item.year}"
    text = dateString
}

@BindingAdapter("penyebab")
fun TextView.setPenyebab(item : PositiveEmotion){
    text = item.what
}

@BindingAdapter("tipe")
fun TextView.setTipe(item : PositiveEmotion){
    text = resources.getString(R.string.positive_emotion_type_formatting , item.type)
}