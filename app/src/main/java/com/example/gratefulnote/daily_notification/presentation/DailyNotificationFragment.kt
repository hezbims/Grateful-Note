package com.example.gratefulnote.daily_notification.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.common.presentation.ResponseWrapperLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Agar bisa ngeinject viewmodel
class DailyNotificationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DailyNotificationScreen()
            }
        }
    }
}

@Composable
fun DailyNotificationScreen(
    viewModel: DailyNotificationViewModel = viewModel()
){
    DailyNotificationScreen(
        state = viewModel.state.collectAsState().value,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun DailyNotificationScreen(
    state : DailyNotificationState,
    onEvent : (DailyNotificationEvent) -> Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ResponseWrapperLoader(
            response = state.listDailyNotification,
            onRetry = {
                onEvent(DailyNotificationEvent.OnLoadListNotification)
            },
            content = {
                LazyColumn {
                    items(it!!){

                    }
                }
            }
        )

        FloatingActionButton(
            onClick = {
                onEvent(DailyNotificationEvent.OnOpenTimePickerDialog)
            },
            modifier = Modifier
                .padding(all = 48.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Buat daily notification baru")
        }
    }

    if (state.openTimePickerDialog)
        ComposeTimePickerDialog(onEvent)
}