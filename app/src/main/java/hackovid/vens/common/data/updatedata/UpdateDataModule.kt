package hackovid.vens.common.data.updatedata

import hackovid.vens.common.data.config.RemoteConfigFirebaseImpl
import hackovid.vens.common.data.filter.StoresDataSource
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val updateDataModule = module {
    factory { UpdateDataDataSource(get()) }
    factory {
        UpdateDataUseCase(
            get(), get<StoresDataSource> { parametersOf(false) }, get(),
            get<RemoteConfigFirebaseImpl>()
        )
    }
}
