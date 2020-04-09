package hackovid.vens.common.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import hackovid.vens.BR

class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: T,
        onItemClick: ((T) -> Unit)? = null,
        onBind: ((T, ViewDataBinding) -> Unit)?
    ) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
        if (onItemClick != null) {
            binding.root.setOnClickListener { onItemClick(item) }
        }
        if (onBind != null) {
            onBind(item, binding)
        }
    }
}
