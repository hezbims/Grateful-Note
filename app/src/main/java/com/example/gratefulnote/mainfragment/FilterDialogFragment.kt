package com.example.gratefulnote.mainfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : DialogFragment() {

    private lateinit var  viewModel : MainViewModel
    private lateinit var binding: FragmentFilterDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainViewModel.getInstance(
            requireActivity().application,
            requireParentFragment()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.listOfYearToString.observe(viewLifecycleOwner) {listOfYearToString ->
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOfYearToString
            ).also {
                binding.yearSpinner.setAdapter(it)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        ArrayAdapter.createFromResource(
            requireContext() ,
            R.array.months_list,
            android.R.layout.simple_spinner_item
        ).also{
            binding.monthSpinner.setAdapter(it)
        }
        ArrayAdapter(
            requireContext() ,
            android.R.layout.simple_spinner_item ,
            viewModel.typeOfPositiveEmotion
        ).also{
            binding.typeOfPESpinner.setAdapter(it)
        }

        if (viewModel.isDialogViewFirstTimeCreated){
            binding.monthSpinner.setText(viewModel.selectedMonth , false)
            binding.yearSpinner.setText(viewModel.selectedYear , false)
            binding.typeOfPESpinner.setText(viewModel.selectedPositiveEmotion , false)
            binding.dateSwitchFilter.isChecked = viewModel.switchState
            viewModel.doneCreatingFirstViewDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentFilterDialogBinding.inflate(layoutInflater)

        return AlertDialog.Builder(
            requireActivity()
        )
            .setView(binding.root)
            .setTitle(R.string.filter)
            .setPositiveButton(R.string.terapkan){
                _ , _ ->
                viewModel.setFilterData(
                    binding.monthSpinner.text.toString(),
                    binding.yearSpinner.text.toString(),
                    binding.typeOfPESpinner.text.toString(),
                    binding.dateSwitchFilter.isChecked
                )
                viewModel.resetDialogState()
            }
            .setNegativeButton(R.string.batal){
                _ , _ ->
                viewModel.resetDialogState()
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.resetDialogState()
    }
}

