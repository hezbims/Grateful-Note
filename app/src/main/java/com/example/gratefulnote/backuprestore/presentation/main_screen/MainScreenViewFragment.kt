package com.example.gratefulnote.backuprestore.presentation.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.gratefulnote.R
import com.example.gratefulnote.mainMenu.presentation.logic.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenViewFragment : Fragment() {
    private val mainViewModel : MainViewModel by navGraphViewModels(R.id.main_crud_graph)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    private fun notifyRestoreSucceedToMainScreen(){
        mainViewModel.refreshDiaryList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                BackupRestoreFragmentBodySetup(
                    notifyRestoreSucceedToMainScreen = ::notifyRestoreSucceedToMainScreen
                )
            }
        }
    }
}