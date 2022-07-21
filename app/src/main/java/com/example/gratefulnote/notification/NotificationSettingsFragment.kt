package com.example.gratefulnote.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment() {

    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding : FragmentNotificationSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this ,
            getViewModelFactory())[NotificationViewModel::class.java]
        binding = FragmentNotificationSettingsBinding.inflate(inflater)
        binding.viewModel = viewModel

        setSwitchCheckedChangeListener()

        return binding.root
    }

    private fun getViewModelFactory() = NotificationViewModelFactory(
        requireNotNull(activity).application
    )

    private fun setSwitchCheckedChangeListener(){
        viewModel.isSwitchChecked.observe(viewLifecycleOwner){
            binding.notificationTimeViewgroup.visibility =
                if (it)View.VISIBLE
                else View.GONE
        }
        binding.switchNotification.setOnCheckedChangeListener{_ , isChecked ->
            viewModel.updateSwitchStatus(isChecked)
        }
    }

}