package hackovid.vens.features.about

import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.databinding.FragmentAboutBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    override val layoutRes = R.layout.fragment_about

    private val viewModel: AboutViewModel by viewModel()

    override fun setupBinding(binding: FragmentAboutBinding) {
        binding.viewModel = viewModel
        setupToolbar(binding)
    }

    private fun setupToolbar(binding: FragmentAboutBinding) {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }
}
