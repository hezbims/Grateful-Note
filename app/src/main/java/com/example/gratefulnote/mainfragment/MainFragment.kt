package com.example.gratefulnote.mainfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var adapter : PositiveAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_main , container , false)

        viewModel = ViewModelProvider(this , getViewModelFactory())[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // RecyclerView Adapter
        adapter = PositiveAdapter(viewModel)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        setNavigateToAddGratitudeFragment()
        setRecyclerViewDataObserver()
        addDatePickerListener()
        onClickDatePicker()

        return binding.root
    }

    private fun getViewModelFactory() : MainViewModelFactory{
        val application = requireNotNull(this.activity).application
        val dataSource = PositiveEmotionDatabase.getInstance(application).positiveEmotionDatabaseDao
        return MainViewModelFactory(dataSource)
    }

    private fun setNavigateToAddGratitudeFragment(){
        viewModel.eventMoveToAddGratitude.observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_mainFragment_to_addGratetitudeFragment)
                viewModel.doneNavigating()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerViewDataObserver(){
        viewModel.recyclerViewData.observe(viewLifecycleOwner){
            adapter.data = it
        }
    }

    private fun addDatePickerListener(){
        datePicker.addOnPositiveButtonClickListener {selectedDate : Long ->
            viewModel.setDateString(
                SimpleDateFormat("dd/M/yyyy" , Locale.getDefault()).format(Date(selectedDate))
            )
        }

        datePicker.addOnCancelListener {
            viewModel.setDateString(getString(R.string.empty_date))
        }

        datePicker.addOnNegativeButtonClickListener {
            viewModel.setDateString(getString(R.string.empty_date))
        }
    }

    private fun onClickDatePicker(){
        binding.selectedDateValue.setOnClickListener{
            datePicker.show(parentFragmentManager  , "Ini Tag")

        }
    }

}