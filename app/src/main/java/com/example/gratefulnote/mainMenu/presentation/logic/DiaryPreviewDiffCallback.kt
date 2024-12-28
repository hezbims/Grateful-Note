package com.example.gratefulnote.mainMenu.presentation.logic

import androidx.recyclerview.widget.DiffUtil
import com.example.gratefulnote.mainMenu.domain.DiaryPreview

class DiaryPreviewDiffCallback : DiffUtil.ItemCallback<DiaryPreview>() {
    override fun areContentsTheSame(oldItem: DiaryPreview, newItem: DiaryPreview): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: DiaryPreview, newItem: DiaryPreview): Boolean {
        return oldItem.id == newItem.id
    }
}