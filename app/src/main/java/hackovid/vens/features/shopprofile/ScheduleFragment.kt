package hackovid.vens.features.shopprofile

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.ui.CustomTimePicker
import hackovid.vens.common.ui.GenericListAdapter
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentScheduleBinding
import hackovid.vens.databinding.ItemScheduleTimerangeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {
    override val layoutRes = R.layout.fragment_schedule

    private val viewModel: ScheduleViewModel by viewModel()

    override fun setupBinding(binding: FragmentScheduleBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
        subscribeToViewModel()
        setupTimeRangeList(binding)
    }

    private fun setupToolbar(binding: FragmentScheduleBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun subscribeToViewModel() {
        observe(viewModel.selectTimesEvent) {
            showTimePicker()
        }
        observe(viewModel.rangeOverlapsError) {
            showErrorSnackbar(R.string.shop_schedule_error_overlapping)
        }
        observe(viewModel.rangeTooShortError) {
            showErrorSnackbar(R.string.shop_schedule_error_range_short)
        }
    }

    private fun showTimePicker() {
        activity?.let { activity ->
            CustomTimePicker()
                .apply { onTimePicked = viewModel::onTimeSelected }
                .show(activity.supportFragmentManager, "CustomTimePicker")
        }
    }

    private fun showErrorSnackbar(errorRes: Int) {
        view?.let { rootView ->
            Snackbar.make(rootView, resources.getString(errorRes), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setupTimeRangeList(binding: FragmentScheduleBinding) {
        val adapter = getTimeRangesListAdapter()
        with(binding.timeRanges) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            this.adapter = adapter
        }
        observe(viewModel.currentTimeRanges) { it.let(adapter::submitList) }
    }

    private fun getTimeRangesListAdapter() = GenericListAdapter(
        getViewLayout = { R.layout.item_schedule_timerange },
        areItemsSame = ::areStoresTheSame,
        areItemContentsEqual = ::areStoresContentsEqual,
        onItemClick = ::onStoreClick,
        onBind = ::onListItemBind
    )

    private fun areStoresTheSame(first: String, second: String) = first == second

    private fun areStoresContentsEqual(first: String, second: String) = first == second

    private fun onStoreClick(item: String) {}

    private fun onListItemBind(item: String, binding: ViewDataBinding) {
        (binding as? ItemScheduleTimerangeBinding)?.let { itemBinding ->
            itemBinding.remove.setOnClickListener {
                viewModel.onRemoveTimeRangeClicked(item)
            }
        }
    }
}