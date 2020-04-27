package hackovid.vens.common.data.updatedata

import org.koin.dsl.module

val updateDataModule = module {
    factory { UpdateDataDataSource(get()) }
    factory { UpdateDataUseCase(get(), get(), get()) }
}
