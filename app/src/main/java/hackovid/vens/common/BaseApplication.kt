package hackovid.vens.common

import android.app.Application
import hackovid.vens.common.data.filter.commonFilterModule
import hackovid.vens.common.di.dataModule
import hackovid.vens.common.di.utilitiesModule
import hackovid.vens.common.ui.di.commonUiModule
import hackovid.vens.features.detail.di.detailModule
import hackovid.vens.features.favourites.di.favouritesModule
import hackovid.vens.features.filter.di.filterModule
import hackovid.vens.features.list.di.listModule
import hackovid.vens.features.map.di.mapModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(koinModules())
        }
    }

    private fun koinModules() = listOf(
        dataModule,
        mapModule,
        listModule,
        favouritesModule,
        detailModule,
        utilitiesModule,
        filterModule,
        commonUiModule,
        commonFilterModule
    )
}
