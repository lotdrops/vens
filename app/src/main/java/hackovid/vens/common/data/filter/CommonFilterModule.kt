package hackovid.vens.common.data.filter

import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val commonFilterModule = module {
    factory { (favouritesOnly: Boolean) -> StoresDataSource(favouritesOnly, get()) }
    factory { (emitTemporalSort: Boolean, favouritesOnly: Boolean) ->
        StoresUseCase(get { parametersOf(favouritesOnly) }, emitTemporalSort)
    }
}
