package hackovid.vens.features.register

import androidx.appcompat.widget.SearchView
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.GenericListAdapter
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentSelectStoreBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

typealias SelectStoreListAdapter = GenericListAdapter<SelectableStore>
class SelectStoreFragment : BaseFragment<FragmentSelectStoreBinding>() {
    override val layoutRes = R.layout.fragment_select_store

    private val viewModel: SelectStoreViewModel by viewModel()
    private lateinit var binding: FragmentSelectStoreBinding

    override fun setupBinding(binding: FragmentSelectStoreBinding) {
        this.binding = binding
        binding.viewModel = viewModel
        setupList(binding)
        setupSearchView(binding.searchView)
    }

    private fun setupList(binding: FragmentSelectStoreBinding) {
        val adapter = getListAdapter()
        with(binding.recyclerview) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            this.adapter = adapter
        }
        subscribeUI(adapter)
    }

    private fun getListAdapter() = SelectStoreListAdapter(
        getViewLayout = { R.layout.item_selectable_store },
        areItemsSame = ::areStoresTheSame,
        areItemContentsEqual = ::areStoresContentsEqual,
        onItemClick = ::onStoreClick
    )

    private fun areStoresTheSame(first: SelectableStore, second: SelectableStore) =
        first.store.id == second.store.id

    private fun areStoresContentsEqual(first: SelectableStore, second: SelectableStore) =
        first == second

    private fun onStoreClick(item: SelectableStore) {
        viewModel.onStoreClicked(item.store.id)
    }

    private fun subscribeUI(adapter: SelectStoreListAdapter) {
        observe(viewModel.stores) { it.let(adapter::submitList) }
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?) = false
            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.query.value = query
                return true
            }
        })
    }
}
