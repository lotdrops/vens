package hackovid.vens.common.data.filter

import org.koin.dsl.module

val commonFilterModule = module {
    factory { (favouritesOnly: Boolean) -> StoresDataSource(favouritesOnly, get()) }
}
