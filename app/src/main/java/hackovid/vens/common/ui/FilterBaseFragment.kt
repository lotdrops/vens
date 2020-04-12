package hackovid.vens.common.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import hackovid.vens.R
import hackovid.vens.features.filter.FilterBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class FilterBaseFragment<Binding : ViewDataBinding> : BaseFragment<Binding>() {
    private val sharedViewModel: SharedViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter -> true.also { onFilterClicked() }
        else -> super.onOptionsItemSelected(item)
    }

    protected open fun onFilterClicked() {
        activity?.supportFragmentManager?.let { fragmentManager ->
            FilterBottomSheetFragment().show(
                fragmentManager,
                FilterBottomSheetFragment::class.java.canonicalName as String
            )
        }
    }
}
