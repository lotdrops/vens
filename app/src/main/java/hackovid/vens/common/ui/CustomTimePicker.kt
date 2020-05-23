package hackovid.vens.common.ui

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class CustomTimePicker : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    var onTimePicked: ((h: Int, min: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, 8, 0, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onTimePicked?.invoke(hourOfDay, minute)
    }
}