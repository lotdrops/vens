package hackovid.vens.features.register.di

import com.google.firebase.auth.FirebaseAuth
import hackovid.vens.features.register.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val registerModule = module {
    viewModel { (externalLogin: Boolean, initialEmail: String?, initialName: String?,
                    initialLastName: String?) ->
        RegisterViewModel(externalLogin, initialEmail, initialName, initialLastName, get(), get(), get())
    }
    viewModel { (storeId: Long) -> FillStoreInfoViewModel(storeId, get(), get(), get()) }
    viewModel { SelectStoreViewModel(get { parametersOf(true, false) }) }
    factory { RegisterFieldsValidator() }
    viewModel { LocateStoreOnMapViewModel() }
    factory { RegisterUseCase(FirebaseAuth.getInstance(),get(),get(), get()) }

}
