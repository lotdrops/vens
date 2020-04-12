package hackovid.vens.common.ui

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import hackovid.vens.R
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType

@BindingAdapter("bind:is_selected")
fun setSelected(view: View, selected: Boolean) {
    view.isSelected = selected
}

@BindingAdapter("bind:store_types")
fun setStoreType(view: TextView, store: Store?) {
    if (store != null) {
        view.text = view.resources.getStoretypeText(store.type, store.subtype)
    }
}

@BindingAdapter("bind:store_types")
fun setStoreType(view: TextView, store: StoreListUi?) {
    if (store != null) {
        view.text = view.resources.getStoretypeText(store.type, store.subtype)
    }
}

private fun Resources.getStoretypeText(type: StoreType, subtype: StoreSubtype) =
    "${getString(type.textRes)} - ${getString(subtype.textRes)}"

@BindingAdapter("bind:crowd")
fun setStoreType(view: TextView, crowd: Int?) {
    val textId = when (crowd) {
        0 -> R.string.filter_crowded_small
        1 -> R.string.filter_crowded_medium
        2 -> R.string.filter_crowded_large
        else -> R.string.filter_crowded_unknown
    }
    view.text = view.resources.getText(textId)
}

@BindingAdapter("bind:phones")
fun setPhones(view: TextView, store: Store) {
    view.setPhones(store.phone, store.mobilePhone)
}

@BindingAdapter("bind:phones")
fun setPhones(view: TextView, store: StoreListUi) {
    view.setPhones(store.phone, store.mobilePhone)
}

private fun TextView.setPhones(phone: String?, mobilePhone: String?) {
    visibility = View.VISIBLE
    if (!phone.isNullOrBlank() && !mobilePhone.isNullOrBlank()) {
        text = "${phone} | ${mobilePhone}"
    } else if (!phone.isNullOrBlank()) {
        text = "${phone}"
    } else if (!mobilePhone.isNullOrBlank()) {
        text = "${mobilePhone}"
    } else {
        visibility = View.GONE
    }
}

