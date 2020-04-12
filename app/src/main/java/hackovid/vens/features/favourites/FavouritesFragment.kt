package hackovid.vens.features.favourites

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hackovid.vens.R
import hackovid.vens.common.ui.FilterBaseFragment
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentFavouritesBinding
import hackovid.vens.databinding.ItemStoreBinding
import hackovid.vens.features.list.StoreListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavouritesFragment : FilterBaseFragment<FragmentFavouritesBinding>() {
    override val layoutRes = R.layout.fragment_favourites

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: FavouritesViewModel by viewModel { parametersOf(sharedViewModel) }

    override fun setupBinding(binding: FragmentFavouritesBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
        setupList(binding)
    }

    private fun setupToolbar(binding: FragmentFavouritesBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun setupList(binding: FragmentFavouritesBinding) {
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

    private fun areStoresTheSame(first: StoreListUi, second: StoreListUi) = first.id == second.id

    private fun areStoresContentsEqual(first: StoreListUi, second: StoreListUi) =
        first.name == second.name && first.isFavourite == second.isFavourite

    private fun onStoreClick(item: StoreListUi) {
        NavHostFragment.findNavController(this)
            .navigate(FavouritesFragmentDirections.navToAdDetail(item.id)) }

    private fun onListItemBind(item: StoreListUi, binding: ViewDataBinding) {
        (binding as? ItemStoreBinding)?.let { itemBinding ->
            itemBinding.favourite.setOnClickListener {
                viewModel.onFavouriteClicked(item)
            }
            itemBinding.contact.setOnClickListener {
                showNotDoneDialog()
            }
        }
    }

    private fun showNotDoneDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.generic_error_not_done_yet_title)
            .setMessage(R.string.generic_error_not_done_yet_message)
            .setPositiveButton(R.string.generic_positive_button) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun subscribeUI(adapter: StoreListAdapter) {
        observe(viewModel.stores) { it.let(adapter::submitList) }
    }
}
