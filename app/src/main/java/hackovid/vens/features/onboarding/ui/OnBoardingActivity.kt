package hackovid.vens.features.onboarding.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackovid.vens.R

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }

    override fun onBackPressed() {
        finish()
    }
}
