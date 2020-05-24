package hackovid.vens.features.onboarding.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R
import hackovid.vens.features.onboarding.di.OnBoardingSharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnBoardingActivity : AppCompatActivity() {

    private val viewModel: OnBoardingSharedViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }
}
