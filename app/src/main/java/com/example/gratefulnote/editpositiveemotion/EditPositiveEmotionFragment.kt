package com.example.gratefulnote.editpositiveemotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import com.example.gratefulnote.mainfragment.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
class EditPositiveEmotionFragment : Fragment() {
    private lateinit var binding : FragmentEditPositiveEmotionBinding

    private val viewModel: EditPositiveEmotionViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback {
                factory : EditPositiveEmotionViewModel.Factory ->
                factory.create(
                    EditPositiveEmotionFragmentArgs
                        .fromBundle(requireArguments()).currentPositiveEmotion
                )
            }
        }
    )
    private val mainViewModel : MainViewModel by navGraphViewModels(R.id.main_crud_graph)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPositiveEmotionBinding.inflate(inflater ,
            container , false)
        binding.lifecycleOwner = viewLifecycleOwner

        initEditText(savedInstanceState == null)

        return binding.root
    }

    private fun initEditText(isFirstTimeCreated : Boolean){
        if (isFirstTimeCreated){
            binding.apply {
                editPositiveEmotionTitleValue.setText(viewModel.currentPositiveEmotion.what)
                editPositiveEmotionDescriptionValue.setText(viewModel.currentPositiveEmotion.why)
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
                mainViewModel.fetchRecyclerViewDataWithCurrentFilterState(true)
            }
        }
    }

}