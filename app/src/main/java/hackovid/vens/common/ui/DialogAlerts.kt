package hackovid.vens.common.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hackovid.vens.R

object Dialogs {
    fun showAlert(
        context: Context,
        title: Int? = R.string.generic_error_title,
        message: Int? = R.string.generic_error_message,
        buttonText: Int = R.string.generic_positive_button
    ) {
        MaterialAlertDialogBuilder(context).apply {
            if (title != null) setTitle(title)
            if (message != null) setMessage(message)
            setPositiveButton(buttonText) { dialog, _ -> dialog.dismiss() }
            show()
        }
    }
}