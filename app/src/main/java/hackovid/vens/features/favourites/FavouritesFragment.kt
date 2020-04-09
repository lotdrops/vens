package hackovid.vens.features.favourites

import androidx.databinding.ViewDataBinding
import hackovid.vens.R
import hackovid.vens.common.data.Store
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentFavouritesBinding
import hackovid.vens.databinding.ItemStoreBinding
import hackovid.vens.features.list.StoreListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>() {
    override val layoutRes = R.layout.fragment_favourites

    private val viewModel: FavouritesViewModel by viewModel()

    override fun setupBinding(binding: FragmentFavouritesBinding) {
        binding.viewModel = viewModel
        val adapter = getListAdapter()
        with(binding.list) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            this.adapter = adapter
        }
        subscribeUI(adapter)
    }

    private fun getListAdapter() = StoreListAdapter(
        getViewLayout = { R.layout.item_store },
        areItemsSame = ::areStoresTheSame,
        areItemContentsEqual = ::areStoresContentsEqual,
        onItemClick = ::onStoreClick,
        onBind = ::onListItemBind
    )

    private fun areStoresTheSame(first: Store, second: Store) = first.id == second.id

    private fun areStoresContentsEqual(first: Store, second: Store) =
        first.name == second.name && first.isFavourite == second.isFavourite

    private fun onStoreClick(item: Store) {
        // TODO navigate to detail?
    }

    private fun onListItemBind(item: Store, binding: ViewDataBinding) {
        (binding as? ItemStoreBinding)?.let { itemBinding ->
            itemBinding.favourite.setOnClickListener {
                viewModel.onFavouriteClicked(item)
            }
        }
    }

    private fun subscribeUI(adapter: StoreListAdapter) {
        observe(viewModel.stores) { it.let(adapter::submitList) }
    }
}
