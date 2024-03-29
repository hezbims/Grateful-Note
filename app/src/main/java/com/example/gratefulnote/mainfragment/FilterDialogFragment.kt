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
            binding.yearSpinner.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_simple_item_list ,
                    listOfYearToString
                )
            )
        }

        viewModel.filterState.observe(viewLifecycleOwner){
            if (savedInstanceState == null){
                binding.monthSpinner.setText(it.stringSelectedMonth , false)
                binding.yearSpinner.setText(it.stringSelectedYear , false)
                binding.typeOfPESpinner.setText(it.selectedPositiveEmotion , false)
                binding.dateSwitchFilter.isChecked = it.switchState
                binding.favoriteSwitchFilter.isChecked = it.onlyFavorite
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.monthSpinner.setAdapter(
            ArrayAdapter.createFromResource(
                requireContext() ,
                R.array.months_list,
                R.layout.spinner_simple_item_list
            )
        )

        binding.typeOfPESpinner.setAdapter(
            ArrayAdapter(
                requireContext() ,
                R.layout.spinner_simple_item_list ,
                viewModel.typeOfPositiveEmotion
            )
        )
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
                binding.apply {
                    viewModel.setFilterData(
                        binding.monthSpinner.text.toString(),
                        binding.yearSpinner.text.toString(),
                        binding.typeOfPESpinner.text.toString(),
                        binding.dateSwitchFilter.isChecked,
                        binding.favoriteSwitchFilter.isChecked
                    )
                }
            }
            .setNegativeButton(R.string.batal){
                _ , _ ->
            }
            .create()
    }

}

