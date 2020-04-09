package hackovid.vens.common.di

import hackovid.vens.common.data.core.StoresDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    single { StoresDatabase.getInstance(androidApplication()) }
    factory { get<StoresDatabase>().storeDao() }
}
