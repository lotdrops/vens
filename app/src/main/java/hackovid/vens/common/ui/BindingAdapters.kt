package hackovid.vens.common.ui

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import hackovid.vens.R
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.features.map.ClusterStoreItem
import kotlin.math.max
import kotlin.math.roundToInt

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
fun setStoreType(view: TextView, store: ClusterStoreItem?) {
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

@BindingAdapter("bind:distance")
fun setDistance(view: TextView, distance: Int?) {
    if (distance == null) {
        view.text = view.resources.getText(R.string.list_distance_no_permission)
    } else {
        val isMeters = distance < 1000
        val textDistance = if (isMeters) {
            distance.toString()
        } else {
            "${distance / 1000}.${(distance / 100).rem(10)}"
        }
        val textId = if (isMeters) R.string.list_distance_m else R.string.list_distance_km
        val minutes = distance.distanceToMinutes()
        view.text = view.resources.getString(textId, textDistance, minutes)
    }
}

@BindingAdapter("bind:full_distance")
fun setFullDistance(view: TextView, distance: Int?) {
    if (distance == null) {
        view.text = view.resources.getText(R.string.detail_distance_no_permission)
    } else {
        val isMeters = distance < 1000
        val textDistance = if (isMeters) {
            distance.toString()
        } else {
            "${distance / 1000}.${(distance / 100).rem(10)}"
        }
        val textId = if (isMeters) R.string.detail_distance_m else R.string.detail_distance_km
        val minutes = distance.distanceToMinutes()
        view.text = view.resources.getString(textId, textDistance, minutes)
    }
}

private fun Int.distanceToMinutes(): Int = max(1.0, (this * 4 / 300.0)).roundToInt()

@BindingAdapter("bind:phones")
fun setPhones(view: TextView, store: Store?) {
    if (store != null) {
        view.setPhones(store.phone, store.mobilePhone)
    }
}

@BindingAdapter("bind:phones")
fun setPhones(view: TextView, store: StoreListUi?) {
    if (store != null) {
        view.setPhones(store.phone, store.mobilePhone)
    }
}

@BindingAdapter("bind:phones_visibility")
fun setPhonesVisibility(view: View, store: StoreListUi?) {
    view.visibility = if (!store?.phone.isNullOrBlank() || !store?.mobilePhone.isNullOrBlank()) {
        View.VISIBLE
    } else View.GONE
}

private fun TextView.setPhones(phone: String?, mobilePhone: String?) {
    visibility = View.VISIBLE
    if (!phone.isNullOrBlank() && !mobilePhone.isNullOrBlank()) {
        text = "$phone | $mobilePhone"
    } else if (!phone.isNullOrBlank()) {
        text = "$phone"
    } else if (!mobilePhone.isNullOrBlank()) {
        text = "$mobilePhone"
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("bind:errorText")
fun setErrorText(view: TextInputLayout, errorId: Int?) {
    if (errorId == null || errorId == 0) view.error = null
    else view.error = view.resources.getText(errorId)
}

@BindingAdapter("bind:image")
fun setImage(view: ImageView, imageId: Int?) {
    if (imageId == null || imageId == 0) view.setImageDrawable(null)
    else view.setImageDrawable(ContextCompat.getDrawable(view.context, imageId))
}
