package com.example.gratefulnote.mainfragment

import android.app.AlertDialog
import android.app.Dialog
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
    ): View {
        viewModel.listOfYearToString.observe(viewLifecycleOwner) {listOfYearToString ->
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOfYearToString
            ).also {
                binding.yearSpinner.setAdapter(it)
            }
        }

        viewModel.filterState.observe(viewLifecycleOwner){
            if (savedInstanceState == null){
                binding.monthSpinner.setText(it.stringSelectedMonth , false)
                binding.yearSpinner.setText(it.stringSelectedYear , false)
                binding.typeOfPESpinner.setText(it.selectedPositiveEmotion , false)
                binding.dateSwitchFilter.isChecked = it.switchState
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
            }
            .setNegativeButton(R.string.batal){
                _ , _ ->
            }
            .create()
    }

}

