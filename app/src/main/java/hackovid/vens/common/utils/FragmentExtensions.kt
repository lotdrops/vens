package hackovid.vens.common.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() =
    (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(getAnyWindowToken(), 0)

private fun Fragment.getAnyWindowToken() =
    activity?.currentFocus?.windowToken ?: view?.rootView?.windowToken
