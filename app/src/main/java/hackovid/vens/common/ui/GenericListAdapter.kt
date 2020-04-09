package hackovid.vens.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class GenericListAdapter<T>(
    val getViewLayout: (position: Int) -> Int,
    areItemsSame: (oldItem: T, newItem: T) -> Boolean,
    areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
    private val onItemClick: ((T) -> Unit)? = null,
    private val onBind: ((T, ViewDataBinding) -> Unit)? = null
) : ListAdapter<T, DataBindingViewHolder<T>>(DiffCallback(areItemsSame, areItemContentsEqual)) {

    constructor(
        getViewLayout: (position: Int) -> Int,
        areItemsSame: (oldItem: T, newItem: T) -> Boolean,
        areItemContentsEqual: (oldItem: T, newItem: T) -> Boolean = { old, new -> old == new },
        onItemClick: OnItemClickListener<T>,
        onBind: OnBindListener<T>
    ) : this(getViewLayout, areItemsSame, areItemContentsEqual, onItemClick::onItemClick,
        onBind::onBind)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent,
            false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = getViewLayout(position)

    interface OnItemClickListener<T> {
        fun onItemClick(item: T)
    }

    interface OnBindListener<T> {
        fun onBind(item: T, binding: ViewDataBinding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) =
            holder.bind(getItem(position), onItemClick, onBind)

    class DiffCallback<T>(
        val sameItems: (oldItem: T, newItem: T) -> Boolean,
        val sameItemContents: (oldItem: T, newItem: T) -> Boolean
    ) : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            sameItems(oldItem, newItem)
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return sameItemContents(oldItem, newItem)
        }
    }
}
