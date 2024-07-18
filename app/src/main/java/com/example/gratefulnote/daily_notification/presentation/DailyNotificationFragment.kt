package com.example.gratefulnote.daily_notification.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gratefulnote.R
import com.example.gratefulnote.daily_notification.presentation.components.ComposeTimePickerDialog
import com.example.gratefulnote.daily_notification.presentation.components.ConfirmDeleteDailyNotificationsDialog
import com.example.gratefulnote.daily_notification.presentation.components.DailyNotificationCard
import com.example.gratefulnote.daily_notification.test_tag.DailyNotificationTestTag
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            val menuProvider = object : MenuProvider {
                override fun onMenuItemSelected(menuItem: MenuItem) =
                    when (menuItem.itemId) {
                        android.R.id.home -> {
                            if (viewModel.state.value.isMultiSelectModeActivated){
                                viewModel.onEvent(
                                    DailyNotificationEvent.OnCancelMultiSelectMode
                                )
                                true
                            }
                            else
                                false
                        }
                        else -> false
                    }

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater){}
            }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.state.collect { state ->
                    when (state.multiSelectMenuState) {
                        MultiSelectToolbarMenuState.SHOW -> {
                            viewModel.onEvent(
                                DailyNotificationEvent.OnDoneShowOrUnshowMultiSelectToolbar
                            )
                            requireActivity().addMenuProvider(
                                menuProvider,
                                viewLifecycleOwner,
                                Lifecycle.State.RESUMED,
                            )
                            (requireActivity() as AppCompatActivity)
                                .supportActionBar?.apply {
                                    title = ""
                                    setHomeAsUpIndicator(R.drawable.ic_close)
                                }
                        }
                        MultiSelectToolbarMenuState.UNSHOW -> {
                            viewModel.onEvent(
                                DailyNotificationEvent.OnDoneShowOrUnshowMultiSelectToolbar
                            )
                            requireActivity().removeMenuProvider(menuProvider)
                            (requireActivity() as AppCompatActivity)
                                .supportActionBar?.apply {
                                    setTitle(R.string.daily_reminder_fragment_title)
                                    setHomeAsUpIndicator(0)
                                }
                        }

                        else -> {}
                    }
                }
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
            contentPadding = PaddingValues(all = 24.dp),
            modifier = Modifier.testTag(DailyNotificationTestTag.listDailyNotificationLazyColumn)
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

        if (state.isMultiSelectModeActivated) {
            val totalDeleteCandidate = remember(state.listDailyNotification) {
                state.listDailyNotification.count {
                    it.isSelectedForDeleteCandidate
                }
            }
            val context = LocalContext.current

            Button(
                onClick = {
                    if (totalDeleteCandidate > 0)
                        onEvent(DailyNotificationEvent.OnClickTrashButton)
                    else
                        Toast.makeText(
                            context,
                            "Tolong pilih satu atau lebih item!",
                            Toast.LENGTH_SHORT
                        ).show()
                },
                modifier = Modifier
                    .padding(all = 48.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        vertical = 8.dp,
                    )
                ) {
                    Text(text = "Hapus Item ($totalDeleteCandidate)")

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        Icons.Filled.DeleteOutline,
                        contentDescription = stringResource(R.string.hapus_item_dipilih)
                    )
                }
            }
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
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.buat_daily_notification_baru))
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
                onEvent(DailyNotificationEvent.OnCancelMultiSelectMode)
            else
                onBackPressedDispatcher?.onBackPressed()

            backPressHandled = false
        }
    }
}