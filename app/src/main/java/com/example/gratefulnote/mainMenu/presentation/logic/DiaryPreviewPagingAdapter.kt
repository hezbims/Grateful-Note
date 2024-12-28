package com.example.gratefulnote.mainMenu.presentation.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.gratefulnote.databinding.ViewHolderPreviewDiaryBinding
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import com.example.gratefulnote.mainMenu.presentation.component.DiaryPreviewViewHolder

class DiaryPreviewPagingAdapter(
    private val clickListener: DiaryPreviewViewHolder.ClickListener) :
    PagingDataAdapter<DiaryPreview, DiaryPreviewViewHolder>(DiaryPreviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DiaryPreviewViewHolder(
        binding = ViewHolderPreviewDiaryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false),
        clickListener = clickListener,
    )

    override fun onBindViewHolder(holder: DiaryPreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}