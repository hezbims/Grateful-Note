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
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentAddGratitudeBinding

class AddGratetitudeFragment : Fragment() {

    private lateinit var viewModel : AddGratetitudeViewModel
    private lateinit var binding : FragmentAddGratitudeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_gratitude , container , false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this , getViewModelFactory())[AddGratetitudeViewModel::class.java]
        binding.viewModel = viewModel

        setNavigateToMainObserver()
        setMenu()

        return binding.root
    }

    private fun getViewModelFactory() : AddGratitudeViewModelFactory{
        val application = requireNotNull(this.activity).application
        val dataSource = PositiveEmotionDatabase.getInstance(application).positiveEmotionDatabaseDao
        return AddGratitudeViewModelFactory(dataSource)
    }

    private fun setNavigateToMainObserver(){
        viewModel.navigateToMainFragment.observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_addGratetitudeFragment_to_mainFragment)
                viewModel.doneNavigating()
            }
        }
    }

    private fun setMenu(){
        val arrayAdapter = ArrayAdapter(requireContext() ,
            R.layout.positive_emotion_menu_item ,
            AddGratetitudeViewModel.typeOfPositiveEmotion)
        binding.typeOfPositiveEmotionMenu.setAdapter(arrayAdapter)
    }

}