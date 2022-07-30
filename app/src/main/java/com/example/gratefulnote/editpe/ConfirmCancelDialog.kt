package com.example.gratefulnote.editpe

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.gratefulnote.R

class ConfirmCancelDialog : DialogFragment(){
    private lateinit var viewModel: EditPositiveEmotionViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = EditPositiveEmotionViewModel.getViewModel(requireActivity())

        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.cancel_dialog_content))
            .setPositiveButton(getString(R.string.ya)){ _ , _ ->
                viewModel.navigateBack()
            }
            .setNegativeButton(getString(R.string.gak)){ _ , _ -> }
            .create()

    }

    override fun onDetach() {
        viewModel.closeDialog()
        super.onDetach()
    }
}