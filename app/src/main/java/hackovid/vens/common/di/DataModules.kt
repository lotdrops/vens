package hackovid.vens.common.di

import androidx.preference.PreferenceManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
    factory { Firebase.firestore }
}

val utilitiesModule = module {
    factory { FileReaderUtilities(androidApplication()) }
}
