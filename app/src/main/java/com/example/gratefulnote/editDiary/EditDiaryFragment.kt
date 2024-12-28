package com.example.gratefulnote.editDiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentEditDiaryBinding
import com.example.gratefulnote.mainMenu.presentation.logic.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
class EditDiaryFragment : Fragment() {
    private lateinit var binding : FragmentEditDiaryBinding

    private val viewModel: EditDiaryViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback {
                factory : EditDiaryViewModel.Factory ->
                factory.create(
                    EditDiaryFragmentArgs
                        .fromBundle(requireArguments()).currentDiary
                )
            }
        }
    )
    private val mainViewModel : MainViewModel by navGraphViewModels(R.id.main_crud_graph)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDiaryBinding.inflate(inflater ,
            container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        initEditText(savedInstanceState == null)

        return binding.root
    }

    private fun initEditText(isFirstTimeCreated : Boolean){
        if (isFirstTimeCreated){
            binding.apply {
                editPositiveEmotionTitleValue.setText(viewModel.currentDiary.what)
                editPositiveEmotionDescriptionValue.setText(viewModel.currentDiary.why)
            }
        }
        binding.editPositiveEmotionTitleValue.addTextChangedListener { editable ->
            viewModel.updatePositiveEmotion(what = editable.toString())
        }
        binding.editPositiveEmotionDescriptionValue.addTextChangedListener { editable ->
            viewModel.updatePositiveEmotion(why = editable.toString())
        }
        viewModel.hasNewDataAfterEdit.observe(viewLifecycleOwner){ hasNewDataAfterEdit ->
            if (hasNewDataAfterEdit){
                viewModel.doneHandlingHasNewData()
                mainViewModel.refreshDiaryList()
            }
        }
    }

}