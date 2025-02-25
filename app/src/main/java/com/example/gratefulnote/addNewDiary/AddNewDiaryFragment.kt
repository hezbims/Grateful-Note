package com.example.gratefulnote.addNewDiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.common.presentation.ConfirmDialog
import com.example.gratefulnote.databinding.FragmentAddNewDiaryBinding
import com.example.gratefulnote.mainMenu.presentation.logic.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewDiaryFragment : Fragment() {

    private val viewModel : AddNewDiaryViewModel by viewModels()
    private val mainViewModel : MainViewModel by navGraphViewModels(R.id.main_crud_graph)

    private lateinit var binding : FragmentAddNewDiaryBinding

    private lateinit var cancelDialog : ConfirmDialog
    private lateinit var saveDialog : ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        cancelDialog = ConfirmDialog.getInstance(
            message = getString(R.string.confirm_add_cancel_message),
            requestKey = getString(R.string.confirm_add_cancel_request_key),
            valueKey = getString(R.string.confirm_add_cancel_value_key),
            context = requireActivity().applicationContext
        )

        saveDialog = ConfirmDialog.getInstance(
            message = getString(R.string.confirm_add_save_message),
            requestKey = getString(R.string.confirm_add_save_request_key),
            valueKey = getString(R.string.confirm_add_save_value_key),
            context = requireContext().applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_new_diary , container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        setSpinner()
        setBackToMain()
        setNavigateToHelp()
        setOnBackPressedListener()
        setDialogListener()

        return binding.root
    }

    private fun setDialogListener(){
        // cancel add gratitude dialog result listener
        childFragmentManager.setFragmentResultListener(
            getString(R.string.confirm_add_cancel_request_key),
            this
        ){
            _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_add_cancel_value_key)))
                findNavController().navigateUp()
        }

        // save dialog result listener
        childFragmentManager.setFragmentResultListener(
            getString(R.string.confirm_add_save_request_key),
            this
        ){
            _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_add_save_value_key))) {
                viewModel.insert(
                    title = binding.whatValue.text.toString(),
                    description = binding.whyValue.text.toString(),
                    tag = binding.emotionTypeSpinner.selectedItem.toString(),
                )
            }
        }
    }

    private fun setBackToMain(){
        viewModel.backToMain.observe(viewLifecycleOwner){
            if (it == true) {
                mainViewModel.refreshDiaryList()
                val navController = findNavController()
                navController.navigateUp()
            }
        }
    }


    private fun setSpinner(){
        binding.emotionTypeSpinner.adapter = ArrayAdapter(requireContext() ,
            R.layout.spinner_simple_item_list ,
            resources.getStringArray(R.array.type_of_positive_emotion_array)
        )

    }

    private fun setNavigateToHelp(){
        viewModel.navigateToHelp.observe(viewLifecycleOwner){
            if (it == true) {
                findNavController().navigate(R.id.action_addGratetitudeFragment_to_topeHelpFragment)
                viewModel.doneNavigatingToHelp()
            }
        }
    }

    private fun allTextEmpty() =
        binding.whatValue.text!!.isEmpty() && binding.whyValue.text!!.isEmpty()

    private fun showCancelDialog(){
        if (!cancelDialog.isAdded)
            cancelDialog.show(childFragmentManager , "TAG")
    }

    private fun setOnBackPressedListener(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if (!allTextEmpty())
                showCancelDialog()
            else if (isEnabled){
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            if (!allTextEmpty())
                showCancelDialog()
            else return super.onOptionsItemSelected(item)
        }
        else{
            if (!saveDialog.isAdded){
                saveDialog.show(childFragmentManager , "TAG")
            }
        }
        return true
    }
}