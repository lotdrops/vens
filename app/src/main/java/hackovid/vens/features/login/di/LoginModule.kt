package hackovid.vens.features.login.di

import com.google.firebase.auth.FirebaseAuth
import hackovid.vens.common.data.login.FirebaseDataSource
import hackovid.vens.common.data.login.FirebaseErrorMapper
import hackovid.vens.common.data.login.RemoteDataSource
import hackovid.vens.features.login.GdprViewModel
import hackovid.vens.features.login.LoginViewModel
import hackovid.vens.features.login.SelectLoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { SelectLoginViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { GdprViewModel(get()) }
    single {
        FirebaseDataSource(FirebaseAuth.getInstance(), get(), get()) as RemoteDataSource
    }
    factory {
        FirebaseErrorMapper()
    }
}
