package hackovid.vens.common.di

import androidx.preference.PreferenceManager
import hackovid.vens.common.data.LocalDataSource
import hackovid.vens.common.data.core.StoresDatabase
import hackovid.vens.common.data.json.LocalJsonPersistency
import hackovid.vens.common.data.json.MoshiFactory
import hackovid.vens.common.data.json.RemoteStore
import hackovid.vens.common.utils.FileReaderUtilities
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { StoresDatabase.getInstance(androidApplication()) }
    factory { get<StoresDatabase>().storeDao() }
    factory {
        LocalJsonPersistency(get(), MoshiFactory.getInstance()) as LocalDataSource<RemoteStore>
    }
    factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

val utilitiesModule = module {
    factory { FileReaderUtilities(androidApplication()) }
}
