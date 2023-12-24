package com.example.gratefulnote.editpositiveemotion

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding

class EditPositiveEmotion : Fragment() {
    private lateinit var binding : FragmentEditPositiveEmotionBinding

    private lateinit var viewModel: EditPositiveEmotionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = EditPositiveEmotionViewModel.getViewModel(this)

    }

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