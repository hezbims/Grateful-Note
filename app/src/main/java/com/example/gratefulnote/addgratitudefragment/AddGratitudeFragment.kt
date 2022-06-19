package com.example.gratefulnote.addgratitudefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentAddGratitudeBinding

class AddGratitudeFragment : Fragment() {

    private lateinit var viewModel : AddGratitudeViewModel
    private lateinit var binding : FragmentAddGratitudeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_gratitude , container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this , getViewModelFactory())[AddGratitudeViewModel::class.java]
        binding.viewModel = viewModel

        setMenu()
        setSubmitListener()
        setBackToMain()
        setNavigateToHelp()

        return binding.root
    }

    private fun setSubmitListener(){
        binding.submit.setOnClickListener {
            val newData = PositiveEmotion(binding.addGratitudeSpinner.selectedItem.toString() ,
                binding.whatValue.text.toString() ,
                binding.whyValue.text.toString())
            viewModel.insert(newData)
        }
    }

    private fun setBackToMain(){
        viewModel.backToMain.observe(viewLifecycleOwner){
            if (it == true)
                findNavController().navigateUp()
        }
    }

    private fun getViewModelFactory() : AddGratitudeViewModelFactory {
        val application = requireNotNull(this.activity).application
        val dataSource = PositiveEmotionDatabase.getInstance(application).positiveEmotionDatabaseDao
        return AddGratitudeViewModelFactory(dataSource)
    }


    private fun setMenu(){
        val arrayAdapter = ArrayAdapter(requireContext() ,
            R.layout.positive_emotion_menu_item ,
            viewModel.typeOfPositiveEmotion)
        binding.addGratitudeSpinner.adapter = arrayAdapter

    }

    private fun setNavigateToHelp(){
        viewModel.navigateToHelp.observe(viewLifecycleOwner){
            if (it == true) {
                findNavController().navigate(R.id.action_addGratetitudeFragment_to_topeHelpFragment)
                viewModel.doneNavigatingToHelp()
            }
        }

    }

}