package hackovid.vens.features.register

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.NavHostFragment
import hackovid.vens.R
import hackovid.vens.common.data.StoreSubtype
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentFillStoreInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FillStoreInfoFragment : BaseFragment<FragmentFillStoreInfoBinding>() {
    override val layoutRes = R.layout.fragment_fill_store_info

    private val viewModel: FillStoreInfoViewModel by viewModel()

    override fun setupBinding(binding: FragmentFillStoreInfoBinding) {
        binding.viewModel = viewModel
        binding.setupViews()
        subscribeToVm()
    }

    private fun FragmentFillStoreInfoBinding.setupViews() {
        context?.let { context ->
            val subTypes = StoreSubtype.values()
                .map { context.resources.getString(it.textRes) }
                .toTypedArray()
            subtype.setDrowdownEntries(subTypes)
            val types = StoreType.values()
                .map { context.resources.getString(it.textRes) }
                .toTypedArray()
            type.setDrowdownEntries(types)
        }
    }

    private fun subscribeToVm() {
        observe(viewModel.selectStoreEvent) {
            NavHostFragment.findNavController(this).navigate(
                RegisterFragmentDirections.navToSelectStoreFragment()
            )
        }
        observe(viewModel.selectLocationEvent) {
            // TODO create screen and navigate
        }
        observe(viewModel.registerEvent) {
            // TODO create screen and navigate
        }
    }
}

private fun AutoCompleteTextView.setDrowdownEntries(elements: Array<String>) {
    setAdapter((ArrayAdapter(context, R.layout.item_dropdown_standard, elements)))
    threshold = 9999 // bug where only 1 option is displayed with preselection
    inputType = 0
}
