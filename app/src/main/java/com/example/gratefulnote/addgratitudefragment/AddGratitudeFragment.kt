package com.example.gratefulnote.addgratitudefragment

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.confirmdialog.ConfirmDialog
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentAddGratitudeBinding

class AddGratitudeFragment : Fragment() {

    private lateinit var viewModel : AddGratitudeViewModel
    private lateinit var binding : FragmentAddGratitudeBinding

    private lateinit var cancelDialog : ConfirmDialog
    private lateinit var saveDialog : ConfirmDialog

    private lateinit var newData : PositiveEmotion

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
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_add_gratitude , container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this , getViewModelFactory())[AddGratitudeViewModel::class.java]
        binding.viewModel = viewModel

        setSpinner()
        setBackToMain()
        setNavigateToHelp()
        setOnBackPressedListener()
        setDialogListener()

        return binding.root
    }

    private fun setDialogListener(){
        // cancel dialog result listener
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
            if (bundle.getBoolean(getString(R.string.confirm_add_save_value_key)))
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


    private fun setSpinner(){
        binding.addGratitudeSpinner.adapter = ArrayAdapter(requireContext() ,
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
                newData = PositiveEmotion(
                    binding.addGratitudeSpinner.selectedItem.toString(),
                    binding.whatValue.text.toString(),
                    binding.whyValue.text.toString()
                )
                saveDialog.show(childFragmentManager , "TAG")
            }
        }
        return true
    }
}