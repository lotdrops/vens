package hackovid.vens.features.shopprofile.di

import hackovid.vens.features.shopprofile.PendingSlotRequestsViewModel
import hackovid.vens.features.shopprofile.ScheduleViewModel
import hackovid.vens.features.shopprofile.ShopProfileViewModel
import hackovid.vens.features.shopprofile.SlotsConfigViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val shopProfileModule = module {
    viewModel { ShopProfileViewModel() }
    viewModel { SlotsConfigViewModel() }
    viewModel { PendingSlotRequestsViewModel() }
    viewModel { ScheduleViewModel() }
}