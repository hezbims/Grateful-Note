package com.example.gratefulnote.common.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.gratefulnote.R

class ConfirmDialog : DialogFragment() {
    private lateinit var message : String
    private lateinit var requestKey: String
    private lateinit var valueKey : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(requireArguments()){
            message = getString(getString(R.string.message_bundle_key))!!
            requestKey = getString(getString(R.string.requestkey_bundle_key))!!
            valueKey = getString(getString(R.string.valuekey_bundle_key))!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(getString(R.string.ya)){ _ , _ ->
                setResult(true)
            }
            .setNegativeButton(getString(R.string.gak)){_ , _ ->
                setResult(false)
            }
            .create()

    private fun setResult(result : Boolean){
        setFragmentResult(
            requestKey,
            bundleOf(valueKey to result)
        )
    }

    companion object{
        fun getInstance(message : String,
                        requestKey : String,
                        valueKey : String,
                        context : Context
                        ) : ConfirmDialog {
            val dialogFragment = ConfirmDialog()

            dialogFragment.arguments = Bundle().apply {
                putString(context.getString(R.string.message_bundle_key), message)
                putString(context.getString(R.string.requestkey_bundle_key) , requestKey)
                putString(context.getString(R.string.valuekey_bundle_key) , valueKey)
            }

            return dialogFragment
        }
    }
}