package com.example.gratefulnote.editpositiveemotion

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import kotlinx.coroutines.runBlocking

class EditPositiveEmotion : Fragment() {

    private lateinit var binding : FragmentEditPositiveEmotionBinding
    private val newWhat : String
        get() = binding.editPositiveEmotionTitleValue.text.toString()
    private val newWhy : String
        get() = binding.editPositiveEmotionDescriptionValue.text.toString()

    private lateinit var viewModel: EditPositiveEmotionViewModel
    private lateinit var curPositiveEmotion : PositiveEmotion

    private val isDataChanged : Boolean
        get() = newWhat != curPositiveEmotion.what || newWhy != curPositiveEmotion.why

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = EditPositiveEmotionViewModel.getViewModel(this)
        runBlocking {
            curPositiveEmotion = viewModel.getCurrentPositiveEmotion(
                EditPositiveEmotionArgs.fromBundle(requireArguments()).positiveEmotionId
            ).await()
        }
    }

    override fun onDestroy() {
        viewModel.isFirstTimeFragmentCreated = true
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPositiveEmotionBinding.inflate(inflater ,
            container , false)

        if (viewModel.isFirstTimeFragmentCreated) {
            binding.editPositiveEmotionTitleValue.setText(curPositiveEmotion.what)
            binding.editPositiveEmotionDescriptionValue.setText(curPositiveEmotion.why)
            viewModel.isFirstTimeFragmentCreated = false
        }
        binding.lifecycleOwner = viewLifecycleOwner

        setOnBackPressedListener()
        setNavigateBack()
        setDialogResultListener()

        return binding.root
    }

    private fun setNavigateBack(){
        viewModel.navigateBack.observe(viewLifecycleOwner){
            if (it == true) {
                viewModel.doneNavigateBack()
                findNavController().navigateUp()
            }
        }
    }

    private fun setDialogResultListener(){
        val id = EditPositiveEmotionArgs.fromBundle(requireArguments()).positiveEmotionId

        childFragmentManager.setFragmentResultListener(getString(R.string.confirm_edit_save_request_key) , this){
                _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_edit_save_key))) {
                viewModel.updatePositiveEmotion(
                    binding.editPositiveEmotionTitleValue.text.toString(),
                    binding.editPositiveEmotionDescriptionValue.text.toString(),
                    id
                )
            }
            else
                viewModel.closeDialog()
        }

        childFragmentManager.setFragmentResultListener(
            getString(R.string.confirm_edit_cancel_request_key) ,this){
            _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_edit_cancel_key)))
                viewModel.navigateBack()
            else
                viewModel.closeDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isDataChanged){
            if (!viewModel.isDialogOpened) {
                viewModel.openDialog()
                if (item.itemId == R.id.save)
                    ConfirmSaveDialog()
                        .show(childFragmentManager, "TAG")
                else
                    ConfirmCancelDialog().show(childFragmentManager, "TAG")
            }
        }
        else
            viewModel.navigateBack()

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_positive_emotion_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setOnBackPressedListener(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if (isDataChanged){
                if (!viewModel.isDialogOpened){
                    viewModel.openDialog()
                    ConfirmCancelDialog().show(childFragmentManager , "TAG")
                }
            }
            else if (isEnabled){
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

}