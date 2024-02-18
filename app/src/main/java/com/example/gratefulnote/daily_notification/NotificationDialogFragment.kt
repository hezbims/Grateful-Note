package com.example.gratefulnote.daily_notification

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.gratefulnote.R
import java.util.*

class NotificationDialogFragment : DialogFragment() , TimePickerDialog.OnTimeSetListener{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity , this , hour , minute , is24HourFormat(activity))
    }

    override fun onTimeSet(view : TimePicker?, hourOfDay : Int , minute : Int) {
        setFragmentResult(
            getString(R.string.clock_picker_request_key) ,
            bundleOf(getString(R.string.clock_picker_clock_key) to Clock(hourOfDay , minute))
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        setFragmentResult(
            getString(R.string.clock_picker_request_key),
            bundleOf(getString(R.string.clock_picker_cancel_key) to true)
        )
    }
}