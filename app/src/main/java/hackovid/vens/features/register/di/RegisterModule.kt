package hackovid.vens.features.register.di

import com.google.firebase.auth.FirebaseAuth
import hackovid.vens.common.data.login.User
import hackovid.vens.features.register.FillStoreInfoViewModel
import hackovid.vens.features.register.LocateStoreOnMapViewModel
import hackovid.vens.features.register.RegisterFieldsValidator
import hackovid.vens.features.register.RegisterUseCase
import hackovid.vens.features.register.RegisterViewModel
import hackovid.vens.features.register.SelectStoreViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val registerModule = module {
    viewModel { (externalLogin: Boolean, initialEmail: String?, initialName: String?,
                    initialLastName: String?) ->
        RegisterViewModel(
            externalLogin, initialEmail, initialName, initialLastName, get(), get(), get()
        )
    }
    viewModel { (storeId: Long, user: User) ->
        FillStoreInfoViewModel(storeId, user, get(), get(), get(), get(), get())
    }
    viewModel { SelectStoreViewModel(get { parametersOf(true, false) }) }
    factory { RegisterFieldsValidator() }
    viewModel { LocateStoreOnMapViewModel() }
    factory { RegisterUseCase(FirebaseAuth.getInstance(), get(), get(), get()) }
}
