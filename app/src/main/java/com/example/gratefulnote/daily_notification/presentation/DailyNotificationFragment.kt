package com.example.gratefulnote.daily_notification.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel

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

}

@Composable
fun DailyNotificationScreen(

){

}