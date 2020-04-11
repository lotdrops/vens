package hackovid.vens.common.ui

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:is_selected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}
