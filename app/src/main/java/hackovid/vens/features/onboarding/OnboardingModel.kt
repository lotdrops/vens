package hackovid.vens.features.onboarding

import android.view.View

data class OnboardingModel(
    val title: Int = 0,
    val content: Int = 0,
    val hasNextScreen: Boolean = false,
    val discoverIconVisibility: Int = View.GONE,
    val nextScreenIconVisibility: Int = View.GONE
)
