package com.example.gratefulnote.editpe

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotion
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import kotlinx.coroutines.runBlocking

class EditPositiveEmotion : Fragment() {

    private lateinit var binding : FragmentEditPositiveEmotionBinding
    private lateinit var viewModel: EditPositiveEmotionViewModel
    private lateinit var curPositiveEmotion : PositiveEmotion
    private var id = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = EditPositiveEmotionArgs.fromBundle(requireArguments()).positiveEmotionId
        viewModel = EditPositiveEmotionViewModel.getViewModel(requireActivity())
        runBlocking {
            curPositiveEmotion = viewModel.getCurrentPositiveEmotion(id).await()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isFirstTimeFragmentCreated = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = FragmentEditPositiveEmotionBinding.inflate(inflater ,
            container , false)
        if (viewModel.isFirstTimeFragmentCreated) {
            binding.editPositiveEmotionTitleValue.setText(curPositiveEmotion.what)
            binding.editPositiveEmotionDescriptionValue.setText(curPositiveEmotion.why)
            viewModel.isFirstTimeFragmentCreated = false
        }
        binding.lifecycleOwner = viewLifecycleOwner

        setNavigateBack()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_positive_emotion_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val newWhat = binding.editPositiveEmotionTitleValue.text.toString()
        val newWhy = binding.editPositiveEmotionDescriptionValue.text.toString()

        if (newWhat != curPositiveEmotion.what || newWhy != curPositiveEmotion.why){
            if (!viewModel.isDialogOpened) {
                viewModel.openDialog()
                if (item.itemId == R.id.save)
                    ConfirmSaveDialog(newWhat, newWhy, id)
                        .show(parentFragmentManager, "TAG")
                else
                    ConfirmCancelDialog().show(parentFragmentManager, "TAG")
            }
        }
        else
            viewModel.navigateBack()

        return true
    }
}