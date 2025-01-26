package com.example.gratefulnote.editDiary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.common.diary.domain.model.DiaryDetails
import com.example.gratefulnote.common.domain.ResponseWrapper
import com.example.gratefulnote.databinding.FragmentEditDiaryBinding
import com.example.gratefulnote.mainMenu.presentation.logic.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditDiaryFragment : Fragment() {
    private lateinit var binding : FragmentEditDiaryBinding

    private val viewModel: EditDiaryViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback {
                factory : EditDiaryViewModel.Factory ->
                factory.create(
                    EditDiaryFragmentArgs
                        .fromBundle(requireArguments()).diaryId
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

        editVisibility()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obserViewState()
    }

    private fun obserViewState(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentDiaryResponse
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .collect { getDiaryDetailsResponse ->
                    viewModel.doneCollectCurrentDiary()
                    when (getDiaryDetailsResponse) {
                        is ResponseWrapper.Succeed -> setDiaryTextFieldValue(getDiaryDetailsResponse.data)
                        is ResponseWrapper.Error -> displayError(getDiaryDetailsResponse.exception)
                        is ResponseWrapper.Loading -> displayLoading()
                        null -> Unit
                    }
                }
        }
    }

    private fun updateDiaryToDb(){
        viewModel.updateDiaryDetails(
            what = binding.editPositiveEmotionTitleValue.editableText.toString(),
            why = binding.editPositiveEmotionDescriptionValue.editableText.toString()
        )
    }

    private fun setDiaryTextFieldValue(diary: DiaryDetails){
        binding.apply {
            editPositiveEmotionTitleValue.setText(diary.title)
            editPositiveEmotionDescriptionValue.setText(diary.description)
            editPositiveEmotionTitleValue.addTextChangedListener { _ ->
                updateDiaryToDb()
            }
            editPositiveEmotionDescriptionValue.addTextChangedListener { _ ->
                updateDiaryToDb()
            }
        }
        editVisibility(editFieldVisibility = View.VISIBLE)
    }

    private fun displayError(exception : Throwable?){
        binding.errorText.text = exception?.message ?: "Unknown error occured"
        editVisibility(errorGroupVisibility = View.VISIBLE)
    }

    private fun displayLoading(){
        editVisibility(loadingIndicatorVisibiltiy = View.VISIBLE)
    }

    private fun editVisibility(
        editFieldVisibility : Int = View.GONE,
        errorGroupVisibility : Int = View.GONE,
        loadingIndicatorVisibiltiy : Int = View.GONE
    ){
        binding.apply {
            editFieldGroup.visibility = editFieldVisibility
            errorGroup.visibility = errorGroupVisibility
            loadingIndicator.visibility = loadingIndicatorVisibiltiy
        }
    }

    override fun onStop() {
        if (viewModel.hasNewDataAfterEdit) {
            viewModel.doneHandlingHasNewData()
            mainViewModel.refreshDiaryList(scrollToTop = true)
        }
        super.onStop()
    }
}