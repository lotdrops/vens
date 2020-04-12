package hackovid.vens.features.filter

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import hackovid.vens.R
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.ui.SharedViewModel
import hackovid.vens.common.utils.observe
import hackovid.vens.databinding.FragmentFilterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val viewModel: FilterBottomSheetViewModel by viewModel { parametersOf(sharedViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentFilterBinding>(
            inflater, R.layout.fragment_filter, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.categoriesChipgroup.addChips()
        bindUi(binding)
        observe(viewModel.closeFilter) { dismiss() }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener { dialogInterface ->
                setupFullHeight(dialogInterface as BottomSheetDialog)
            }
        }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val dialogHeight = getWindowHeight() - getStatusBarHeight()
        if (layoutParams != null) {
            layoutParams.height = dialogHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun ChipGroup.addChips() {
        StoreType.values().forEachIndexed() { position, storeType ->
            addView(Chip(context).apply {
                text = resources.getText(storeType.textRes)
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked: Boolean ->
                    viewModel.onCategorySelected(position, isChecked)
                }
            })
        }
    }

    private fun bindUi(binding: FragmentFilterBinding) {
        observe(viewModel.categories) { categoriesChecked ->
            binding.categoriesChipgroup.children.forEachIndexed { index, view ->
                (view as? Chip)?.isChecked = categoriesChecked[index]
            }
        }
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun getStatusBarHeight(): Int {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) resources.getDimensionPixelSize(id) else 0
    }
}
