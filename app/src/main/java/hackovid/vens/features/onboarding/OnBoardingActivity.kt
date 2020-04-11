package hackovid.vens.features.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R
import org.koin.android.ext.android.inject

class OnBoardingActivity : AppCompatActivity() {

    private val viewModel:OnBoardingViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }

}
