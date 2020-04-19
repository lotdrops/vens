package hackovid.vens.features.onboarding.di

import hackovid.vens.common.data.LocalStorage
import hackovid.vens.common.data.sharedpreferences.SharedPreferencesPersistence
import hackovid.vens.features.onboarding.viewmodel.OnBoardingViewModel
import org.koin.dsl.module

val onBoardingModule = module {
    single { OnBoardingViewModel(get(), get(), get()) }
    factory { SharedPreferencesPersistence(get()) as LocalStorage }
}
