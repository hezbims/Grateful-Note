package com.example.gratefulnote.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gratefulnote.R
import com.example.gratefulnote.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment : Fragment() {

    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding : FragmentNotificationSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this ,
            getViewModelFactory())[NotificationViewModel::class.java]

        childFragmentManager.setFragmentResultListener(
            getString(R.string.clock_picker_request_key),
            this
        ){_ , bundle ->
            if (bundle.getBoolean(getString(R.string.clock_picker_cancel_key) , false))
                viewModel.closeDialog()
            else
                viewModel.setSavedClock(bundle.getParcelable(getString(R.string.clock_picker_clock_key))!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationSettingsBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setSwitchCheckedChangeListener()
        setOpenDialogListener()

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
            viewModel.setAlarmNotification()
        }
        binding.switchNotification.setOnCheckedChangeListener{_ , isChecked ->
            viewModel.updateSwitchStatus(isChecked)
        }
    }

    private fun setOpenDialogListener(){
        binding.muculSetiap.setOnClickListener { openDialog() }
        binding.notificationTimeText.setOnClickListener { openDialog() }
    }

    private fun openDialog(){
        if (!viewModel.isDialogOpened){
            viewModel.openDialog()
            NotificationDialogFragment().show(childFragmentManager , "timePicker")
        }
    }

}