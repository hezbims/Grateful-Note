package com.example.gratefulnote.editpositiveemotion

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.confirmdialog.ConfirmDialog
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import kotlinx.coroutines.runBlocking

class EditPositiveEmotion : Fragment() {
    private lateinit var binding : FragmentEditPositiveEmotionBinding

    private lateinit var viewModel: EditPositiveEmotionViewModel
    private lateinit var curPositiveEmotion : PositiveEmotion

    private val newWhat : String
        get() = binding.editPositiveEmotionTitleValue.text.toString()
    private val newWhy : String
        get() = binding.editPositiveEmotionDescriptionValue.text.toString()
    private val isDataChanged : Boolean
        get() =
            newWhat != curPositiveEmotion.what || newWhy != curPositiveEmotion.why

    private lateinit var cancelDialog: ConfirmDialog
    private lateinit var saveDialog : ConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = EditPositiveEmotionViewModel.getViewModel(this)

        saveDialog = ConfirmDialog.getInstance(
            getString(R.string.confirm_edit_save_content),
            getString(R.string.confirm_edit_save_request_key),
            getString(R.string.confirm_edit_save_value_key),
            requireActivity().application.applicationContext
        )

       cancelDialog = ConfirmDialog.getInstance(
            getString(R.string.confirm_edit_cancel_content),
            getString(R.string.confirm_edit_cancel_request_key),
            getString(R.string.confirm_edit_cancel_value_key),
            requireActivity().application.applicationContext
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPositiveEmotionBinding.inflate(inflater ,
            container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        setOnBackPressedListener()
        setNavigateBack()
        setDialogResultListener()
        setFirstTimeCreated(savedInstanceState == null)

        return binding.root
    }

    private fun setFirstTimeCreated(isFirstTimeCreated : Boolean){
        curPositiveEmotion = runBlocking {
            viewModel.getCurrentPositiveEmotion().await()
        }

        if (isFirstTimeCreated){
            binding.apply {
                editPositiveEmotionTitleValue.setText(curPositiveEmotion.what)
                editPositiveEmotionDescriptionValue.setText(curPositiveEmotion.why)
            }
        }
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
        childFragmentManager.setFragmentResultListener(getString(R.string.confirm_edit_save_request_key) , this){
                _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_edit_save_value_key))) {
                viewModel.updatePositiveEmotion(
                    curPositiveEmotion.copy(
                       what = binding.editPositiveEmotionTitleValue.text.toString(),
                        why = binding.editPositiveEmotionDescriptionValue.text.toString()
                    )
                )
            }
        }

        childFragmentManager.setFragmentResultListener(
            getString(R.string.confirm_edit_cancel_request_key) ,this){
            _ , bundle ->
            if (bundle.getBoolean(getString(R.string.confirm_edit_cancel_value_key)))
                viewModel.navigateBack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isDataChanged){
            if (item.itemId == R.id.save){
                if (!saveDialog.isAdded)
                    saveDialog.show(childFragmentManager , "TAG")
            }
            else{
                if (!cancelDialog.isAdded)
                    cancelDialog.show(childFragmentManager , "TAG")
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
                if (!cancelDialog.isAdded)
                    cancelDialog.show(childFragmentManager , "TAG")
            }
            else if (isEnabled){
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

}