package com.example.gratefulnote.notification

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

class NotificationDialogFragment : DialogFragment() , TimePickerDialog.OnTimeSetListener{
    private lateinit var viewModel: NotificationViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(requireActivity() ,
            getViewModelFactory())[NotificationViewModel::class.java]

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity , this , hour , minute , is24HourFormat(activity))
    }

    override fun onTimeSet(view : TimePicker?, hourOfDay : Int , minute : Int) {
        viewModel.setLiveClock(hourOfDay , minute)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        viewModel.setDialogOpenedStatus(false)
    }

    private fun getViewModelFactory() = NotificationViewModelFactory(
        requireNotNull(activity).application
    )
}