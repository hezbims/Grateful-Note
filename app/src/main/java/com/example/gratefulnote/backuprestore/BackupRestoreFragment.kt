package com.example.gratefulnote.backuprestore

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class BackupRestoreFragment : Fragment() {
//    private val createJSONIntent = registerForActivityResult(
//        CreateJSONContract()
//    ){
//        if (it != null)
//            viewModel.backup(it)
//    }
//
//    private val restoreJsonIntent = registerForActivityResult(
//        RestoreJSONContract()
//    ){
//        if (it != null)
//            viewModel.restore(it)
//    }

    private lateinit var viewModel : BackupRestoreViewModel
    private lateinit var getDocumentTreeAction : ActivityResultLauncher<Uri?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(
            this ,
            BackupRestoreViewModelFactory(requireActivity().application)
        )[BackupRestoreViewModel::class.java]

        getDocumentTreeAction = registerForActivityResult(
            ActivityResultContracts.OpenDocumentTree()
        ){
            if (it != null){
                viewModel.onEvent(BackupRestoreStateEvent.EventUpdatePathLocation(it))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply{
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

            }
        }
    }

    fun onActivityResultEvent(event : BackupRestoreActivityEvent){
        when (event){
            BackupRestoreActivityEvent.OpenDocumentTree ->
                getDocumentTreeAction.launch(null)
        }
    }
}

sealed class BackupRestoreActivityEvent {
    object OpenDocumentTree : BackupRestoreActivityEvent()
}