package com.example.gratefulnote.addgratitudefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentAddGratitudeBinding

class AddGratetitudeFragment : Fragment() {

    private lateinit var viewModel: AddGratetitudeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentAddGratitudeBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_gratitude , container , false)
        binding.lifecycleOwner = this

        // get view model factory
        val application = requireNotNull(this.activity).application
        val dataSource = PositiveEmotionDatabase.getInstance(application).positiveEmotionDatabaseDao
        val viewModelFactory = AddGratitudeViewModelFactory(dataSource)

        viewModel = ViewModelProvider(this , viewModelFactory)[AddGratetitudeViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.navigateToMainFragment.observe(viewLifecycleOwner){
            if (it == true){
                findNavController().navigate(R.id.action_addGratetitudeFragment_to_mainFragment)
                viewModel.doneNavigating()
            }
        }

        return binding.root
    }


}