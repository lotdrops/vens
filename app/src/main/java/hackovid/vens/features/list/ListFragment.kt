package hackovid.vens.features.list

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import hackovid.vens.R
import hackovid.vens.common.ui.FilterBaseFragment
import hackovid.vens.common.ui.GenericListAdapter
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.ui.StoreListUi
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentListBinding
import hackovid.vens.databinding.ItemStoreBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

typealias StoreListAdapter = GenericListAdapter<StoreListUi>
class ListFragment : FilterBaseFragment<FragmentListBinding>() {
    override val layoutRes = R.layout.fragment_list

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: ListViewModel by viewModel { parametersOf(sharedViewModel) }

    override fun setupBinding(binding: FragmentListBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
        setupList(binding)
    }

    private fun setupToolbar(binding: FragmentListBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }

    private fun setupList(binding: FragmentListBinding) {
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

    private fun areStoresContentsEqual(first: StoreListUi, second: StoreListUi) = first == second

    private fun onStoreClick(item: StoreListUi) {
        NavHostFragment.findNavController(this)
                .navigate(ListFragmentDirections.navToAdDetail(item.id))
    }

    private fun onListItemBind(item: StoreListUi, binding: ViewDataBinding) {
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
