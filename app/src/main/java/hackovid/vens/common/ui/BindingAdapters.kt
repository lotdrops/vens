package hackovid.vens.common.ui

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import hackovid.vens.common.data.Store

@BindingAdapter("bind:is_selected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter("bind:store_types")
fun setStoreType(view: TextView, store: Store) {
    view.text = "${view.resources.getString(store.type.textRes)} - ${view.resources.getString(store.subtype.textRes)}"
}
