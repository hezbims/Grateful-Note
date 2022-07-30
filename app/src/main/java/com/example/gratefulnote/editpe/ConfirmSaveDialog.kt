package com.example.gratefulnote.editpe

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.gratefulnote.R

class ConfirmSaveDialog(private val newWhat : String,
                        private val newWhy : String,
                        private val id : Long) : DialogFragment(){
    private lateinit var viewModel : EditPositiveEmotionViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = EditPositiveEmotionViewModel.getViewModel(requireActivity())

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.confirm_save_content))
            .setPositiveButton(getString(R.string.ya)){
                _ , _ ->
                viewModel.updatePositiveEmotion(newWhat , newWhy , id)
            }
            .setNegativeButton(getString(R.string.gak)){
                _ , _ ->
            }
            .create()
    }

    override fun onDetach() {
        viewModel.closeDialog()
        super.onDetach()
    }
}