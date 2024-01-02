package com.example.gratefulnote.backuprestore

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class BackupRestoreFragment : Fragment() {
    private lateinit var viewModel : BackupRestoreViewModel


    private val createJSONIntent = registerForActivityResult(
        CreateJSONContract()
    ){
        if (it != null)
            viewModel.backup(it)
    }

    private val restoreJsonIntent = registerForActivityResult(
        RestoreJSONContract()
    ){
        if (it != null)
            viewModel.restore(it)
    }

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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val state = viewModel.backupRestoreState.collectAsState().value

                    if (state.pathLocation == null)
                        ElevatedButton(onClick = {
                            getDocumentTreeAction.launch(null)
                        }) {
                            Text(text = "Pilih Lokasi Backup")
                        }

                    else {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {

                        }
                        Card (
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()


                        ) {
                            Column(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .padding(
                                        start = 24.dp,
                                        end = 24.dp,
                                        top = 16.dp,
                                        bottom = 12.dp
                                    )
                            ) {
                                Row(
                                ) {
                                    Button(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Ubah Lokasi")
                                    }
                                    Button(
                                        onClick = { /*TODO*/ },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(text = "Buat Backup Baru")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}