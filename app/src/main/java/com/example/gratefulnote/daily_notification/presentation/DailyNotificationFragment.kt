package com.example.gratefulnote.daily_notification.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.daily_notification.presentation.components.ComposeTimePickerDialog
import com.example.gratefulnote.daily_notification.presentation.components.ConfirmDeleteDailyNotificationsDialog
import com.example.gratefulnote.daily_notification.presentation.components.DailyNotificationCard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

@AndroidEntryPoint // Agar bisa ngeinject viewmodel
class DailyNotificationFragment : Fragment() {
    private val viewModel : DailyNotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DailyNotificationScreen(
                    viewModel = viewModel,
                )
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
    HandleCustomBackPress(
        state = state,
        onEvent = onEvent,
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(all = 24.dp)
        ) {
            items(state.listDailyNotification, key = {item ->  item.data.id}){ item ->
                DailyNotificationCard(
                    dailyNotificationUiModel = item,
                    onLongClick = {
                      onEvent(DailyNotificationEvent
                        .OnLongClickDailyNotificationCard(item.data))
                    },
                    onClickWhenSelectModeActivated =
                        if (state.isMultiSelectModeActivated)({
                            onEvent(DailyNotificationEvent.OnClickItemWhenMultiSelectModeActivated(
                                dailyNotification = item,
                            ))
                        })
                        else null,
                    onToogleSwitch = {
                        onEvent(DailyNotificationEvent.OnToogleSwithListItem(item.data))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (state.isMultiSelectModeActivated)
            FloatingActionButton(
                onClick = {
                    onEvent(DailyNotificationEvent.OnClickTrashButton)
                },
                modifier = Modifier
                    .padding(all = 48.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Hapus item dipilih")
            }

        else
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
        ComposeTimePickerDialog(
            state = state,
            onEvent = onEvent,
        )
    if (state.openConfirmDeleteDialog)
        ConfirmDeleteDailyNotificationsDialog(
            state = state,
            onEvent = onEvent,
        )
}

@Composable
fun HandleCustomBackPress(
    state: DailyNotificationState,
    onEvent: (DailyNotificationEvent) -> Unit,
){
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(
        enabled = !backPressHandled
    ) {
        backPressHandled = true
        coroutineScope.launch {
            awaitFrame()
            if (state.isMultiSelectModeActivated)
                onEvent(DailyNotificationEvent.OnBackPressWhenMultiSelectModeActivated)
            else
                onBackPressedDispatcher?.onBackPressed()

            backPressHandled = false
        }
    }
}