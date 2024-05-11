package com.example.gratefulnote.mainfragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : DialogFragment() {
    private val viewModel : MainViewModel by viewModels()
    private lateinit var binding: FragmentFilterDialogBinding
    private lateinit var semuaText : String
    private lateinit var monthList : Array<String>
    private lateinit var positiveEmotionTypeList : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFilterDialogBinding.inflate(layoutInflater)
        semuaText = getString(R.string.semua)

        requireContext().resources.apply {
            monthList = getStringArray(R.array.months_list)
            positiveEmotionTypeList = getStringArray(R.array.type_of_positive_emotion_array)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.listOfYear.observe(viewLifecycleOwner) { yearList ->
            binding.yearSpinner.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_simple_item_list ,
                    listOf(semuaText) + yearList.map{it.toString()}
                )
            )
        }

        if (savedInstanceState == null){
            val state = viewModel.filterState
            binding.apply {
                monthSpinner.setText(state.selectedMonth?.let {
                    monthList[it - 1]
                } ?: semuaText,)
                yearSpinner.setText(state.selectedYear?.toString() ?: semuaText)
                typeOfPESpinner.setText(state.positiveEmotionType ?: semuaText)
                isSortedLatestSwitch.isChecked = state.isSortedLatest
                isOnlyFavoriteSwitch.isChecked = state.isOnlyFavorite
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.monthSpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_simple_item_list,
                arrayOf(semuaText, *monthList)
            )
        )

        binding.typeOfPESpinner.setAdapter(
            ArrayAdapter(
                requireContext() ,
                R.layout.spinner_simple_item_list ,
                arrayOf(semuaText, *positiveEmotionTypeList)
            )
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(
            requireActivity()
        )
            .setView(binding.root)
            .setTitle(R.string.filter_dialog_title)
            .setPositiveButton(R.string.terapkan){
                _ , _ ->
                viewModel.setNewFilterData(FilterState(
                    selectedMonth = getSelectedMonthFromSpinner(),
                    selectedYear = binding.yearSpinner.text.toString().toIntOrNull(),
                    positiveEmotionType = getPositiveEmotionTypeFromSpinner(),
                    isSortedLatest = binding.isSortedLatestSwitch.isChecked,
                    isOnlyFavorite = binding.isOnlyFavoriteSwitch.isChecked,
                ))
            }
            .setNegativeButton(R.string.batal){
                _ , _ ->
            }
            .create()
    }

    private fun getSelectedMonthFromSpinner() : Int? {
        val index = monthList.indexOfFirst {
            it == binding.monthSpinner.text.toString()
        }
        return if (index == -1) null else index + 1
    }

    private fun getPositiveEmotionTypeFromSpinner() : String? =
        positiveEmotionTypeList.singleOrNull {
            it == binding.typeOfPESpinner.text.toString()
        }


}

