package hackovid.vens.common.data.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hackovid.vens.common.data.LocalDataSource
import hackovid.vens.common.utils.FileReaderUtilities

class LocalJsonPersistency(
    private val fileReader: FileReaderUtilities,
    private val moshi: Moshi = MoshiFactory.getInstance()
) : LocalDataSource<RemoteStore> {

    private val localJsonStoreFile = "LocalJsonStoresData.json"

    override fun readLocalStoreData(): List<RemoteStore>? {
        val storesJson = fileReader.readJsonFileFromAssets(localJsonStoreFile)
        val storesTypes = Types.newParameterizedType(List::class.java, RemoteStore::class.java)
        val adapter = moshi.adapter<List<RemoteStore>>(storesTypes)
        return adapter.fromJson(storesJson)

    }


}