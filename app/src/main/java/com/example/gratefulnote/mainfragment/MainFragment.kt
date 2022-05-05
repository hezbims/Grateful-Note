package com.example.gratefulnote.mainfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
        Log.d("Debugging" , "onCreateViewDipanggil")

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
        onChangeRecyclerViewData()
        addDatePickerListener()
        onClickDatePicker()
        onChangeDateFilter()
        setMenu()
        onMenuItemChange()

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

    private fun onChangeRecyclerViewData(){
        viewModel.recyclerViewData.observe(viewLifecycleOwner){
            adapter.data = viewModel.recyclerViewData.value!!
        }
    }

    private fun addDatePickerListener(){
        datePicker.addOnPositiveButtonClickListener {selectedDate : Long ->
            viewModel.setDateString(
                SimpleDateFormat("dd/M/yyyy" , Locale.getDefault()).format(Date(selectedDate))
            )
        }

        datePicker.addOnCancelListener {
            viewModel.setDateString()
        }

        datePicker.addOnNegativeButtonClickListener {
            viewModel.setDateString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClickDatePicker(){
        binding.selectedDateValue.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN && !datePicker.isVisible) {
                datePicker.show(parentFragmentManager, "Ini Tag")
            }
            false
        }
    }

    private fun onChangeDateFilter(){
        viewModel.selectedDateString.observe(viewLifecycleOwner){
            viewModel.updateRecyclerViewData()
        }
    }

    private fun setMenu(){
        val arrayAdapter = ArrayAdapter(requireContext() ,
            R.layout.positive_emotion_menu_item ,
            viewModel.typeOfPositiveEmotion)
        binding.positiveEmotionFilterValue.setAdapter(arrayAdapter)
        binding.positiveEmotionFilterValue.setOnItemClickListener{_ , _ , pos , _ ->
            viewModel.setSelectedPositiveEmotion(viewModel.typeOfPositiveEmotion[pos])
        }
    }

    private fun onMenuItemChange(){
        viewModel.selectedPositiveEmotion.observe(viewLifecycleOwner){
            Log.d("Debugging" , "selected positive emotion : ${viewModel.selectedPositiveEmotion.value}")
            viewModel.updateRecyclerViewData()
        }
    }
}