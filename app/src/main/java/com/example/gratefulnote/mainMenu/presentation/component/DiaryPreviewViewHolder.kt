package com.example.gratefulnote.mainMenu.presentation.component

import androidx.recyclerview.widget.RecyclerView
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.ViewHolderPreviewDiaryBinding
import com.example.gratefulnote.mainMenu.domain.DiaryPreview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DiaryPreviewViewHolder(
    private val binding: ViewHolderPreviewDiaryBinding,
    private val clickListener : ClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: DiaryPreview?){
        data?.let { emotion ->
            binding.tanggal.text = dateFormatter.format(
                Calendar.getInstance()
                    .apply {
                        timeInMillis = emotion.createdAt
                    }.time
            )
            binding.tipe.text = emotion.type
            binding.title.text = emotion.title
            binding.editDiaryIcon.setOnClickListener {
                clickListener.onClickEdit(emotion)
            }
            binding.favoriteSymbol.setOnClickListener {
                clickListener.onClickFavorite(emotion)
            }
            binding.favoriteSymbol.apply {
                val nextDrawableId =
                    if (emotion.isFavorite)
                        R.drawable.ic_baseline_yellow_star
                    else
                        R.drawable.ic_outline_star_border
                setImageResource(nextDrawableId)
            }
            binding.deleteDiaryIcon.setOnClickListener {
                clickListener.onClickDelete(emotion)
            }
        }
    }

    companion object {
        val dateFormatter = SimpleDateFormat(
            "dd MMM yyyy", Locale.US)
    }

    data class ClickListener(
        val onClickEdit : (data : DiaryPreview) -> Unit,
        val onClickDelete : (data : DiaryPreview) -> Unit,
        val onClickFavorite : (data : DiaryPreview) -> Unit,
    )
}