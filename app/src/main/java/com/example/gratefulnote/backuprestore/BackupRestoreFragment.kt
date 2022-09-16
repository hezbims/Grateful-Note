package com.example.gratefulnote.backuprestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.databinding.FragmentBackupRestoreBinding

class BackupRestoreFragment : Fragment() {
    private lateinit var viewModel : BackupRestoreViewModel
    private lateinit var binding : FragmentBackupRestoreBinding


    private val createJSONIntent = registerForActivityResult(
        CreateJSON()
    ){
        viewModel.backup(it)
    }

    private val restoreJsonIntent = registerForActivityResult(
        RestoreJSON()
    ){
        viewModel.restore(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(
            this ,
            BackupRestoreViewModelFactory(requireActivity().application)
        )[BackupRestoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupRestoreBinding.inflate(inflater , container , false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.restoreButton.setOnClickListener {
            restoreJsonIntent.launch("")
        }
        binding.backupButton.setOnClickListener {
            createJSONIntent.launch("")
        }

        setOnBackPressed()

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (viewModel.isProcessing)true
        else super.onOptionsItemSelected(item)

    private fun setOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if (!viewModel.isProcessing && isEnabled){
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }
}