package com.example.gratefulnote.editpositiveemotion

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.gratefulnote.R

class ConfirmSaveDialog : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.confirm_save_content))
            .setPositiveButton(getString(R.string.ya)){
                _ , _ ->
                setResult(true)
            }
            .setNegativeButton(getString(R.string.gak)){ _ , _ ->
                setResult(false)
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setResult(false)
    }

    private fun setResult(result : Boolean){
        setFragmentResult(getString(R.string.confirm_edit_save_request_key) ,
            bundleOf(getString(R.string.confirm_edit_save_key) to result)
        )
    }
}