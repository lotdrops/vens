package hackovid.vens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import hackovid.vens.common.ui.BaseFragment
import hackovid.vens.features.onboarding.ui.OnBoardingActivity
import hackovid.vens.features.onboarding.viewmodel.OnBoardingViewModel
import kotlinx.android.synthetic.main.activity_main.nav_host
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val viewModel: OnBoardingViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openOnBoardingScreenIfIsTheFirstTime()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        findNavController(R.id.nav_host).apply {
            val backHandled = getCurrentFragment()?.onBackPressed() ?: false
            if (!backHandled) super.onBackPressed()
        }
    }

    private fun getCurrentFragment(): BaseFragment<*>? {
        return try {
            nav_host?.childFragmentManager?.fragments?.firstOrNull() as? BaseFragment<*>
        } catch (ex: Throwable) {
            null
        }
    }

    private fun openOnBoardingScreenIfIsTheFirstTime() {
        if(viewModel.onBoardScreenShouldBeDisplayed()){
            startActivity(Intent(this, OnBoardingActivity::class.java))
        }
    }
}
