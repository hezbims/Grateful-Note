package com.example.gratefulnote.editpe

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gratefulnote.R
import com.example.gratefulnote.database.PositiveEmotionDatabase
import com.example.gratefulnote.databinding.FragmentEditPositiveEmotionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPositiveEmotion : Fragment() {

    private lateinit var binding : FragmentEditPositiveEmotionBinding
    private lateinit var viewModel: EditPositiveEmotionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this , getViewModelFactory())[EditPositiveEmotionViewModel::class.java]

        binding = FragmentEditPositiveEmotionBinding.inflate(inflater , container , false)

        if (viewModel.isFirstTimeCreated) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val curPositiveEmotion = viewModel.curPositiveEmotion.await()
                withContext(Dispatchers.Main) {
                    binding.editPositiveEmotionTitleValue.setText(curPositiveEmotion.what)
                    binding.editPositiveEmotionDescriptionValue.setText(curPositiveEmotion.why)
                    viewModel.isFirstTimeCreated = false
                }
            }
        }

        setNavigateBack()

        return binding.root
    }

    private fun setNavigateBack(){
        viewModel.navigateBack.observe(viewLifecycleOwner){
            if (it == true)
                findNavController().navigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_positive_emotion_menu , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save)
            viewModel.updatePositiveEmotion(binding.editPositiveEmotionTitleValue.text.toString() ,
                binding.editPositiveEmotionDescriptionValue.text.toString())
        return super.onOptionsItemSelected(item)
    }



    private fun getViewModelFactory() : EditPositiveEmotionViewModelFactory{
        val application = requireNotNull(this.activity).application
        val dataSource = PositiveEmotionDatabase.getInstance(application).positiveEmotionDatabaseDao
        return EditPositiveEmotionViewModelFactory(dataSource ,
            EditPositiveEmotionArgs.fromBundle(requireArguments()).positiveEmotionId
        )
    }


}