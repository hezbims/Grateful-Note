package com.example.gratefulnote.mainfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentMainBinding


class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding : FragmentMainBinding =
            DataBindingUtil.inflate(inflater , R.layout.fragment_main , container , false)

        val viewModel : MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.eventMoveToAddGratitude.observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_mainFragment_to_addGratetitudeFragment)
                viewModel.doneNavigating()
            }
        }

        return binding.root
    }


}