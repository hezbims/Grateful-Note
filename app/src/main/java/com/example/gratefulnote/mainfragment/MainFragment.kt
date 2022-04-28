package com.example.gratefulnote.mainfragment

import android.os.Bundle
import android.util.Log
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


class MainFragment : Fragment() {

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
        adapter = PositiveAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        setNavigateToAddGratitudeFragment()
        setRecyclerViewDataObserver()

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

    private fun setRecyclerViewDataObserver(){
        viewModel.recyclerViewData.observe(viewLifecycleOwner){
            adapter.data = it
        }
    }
}