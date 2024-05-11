package com.example.gratefulnote.editpositiveemotion

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import dagger.hilt.android.lifecycle.withCreationCallback

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
            viewModel.updatePositiveEmotion(viewModel.currentPositiveEmotion.copy(
                what = editable.toString()
            ))
        }
        binding.editPositiveEmotionDescriptionValue.addTextChangedListener { editable: Editable? ->
            viewModel.updatePositiveEmotion(viewModel.currentPositiveEmotion.copy(
                why = editable.toString()
            ))
        }
    }

}