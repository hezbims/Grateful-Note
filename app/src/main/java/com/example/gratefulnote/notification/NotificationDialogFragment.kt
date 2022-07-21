package com.example.gratefulnote.notification

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar

class NotificationDialogFragment : DialogFragment() , TimePickerDialog.OnTimeSetListener{
    private lateinit var viewModel: NotificationViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this ,
            getViewModelFactory())[NotificationViewModel::class.java]

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity , this , hour , minute , is24HourFormat(activity))
    }

    override fun onTimeSet(view : TimePicker?, hourOfDay : Int , minute : Int) {
        TODO("Not yet implemented")
    }

    private fun getViewModelFactory() = NotificationViewModelFactory(
        requireNotNull(activity).application
    )
}