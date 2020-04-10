package hackovid.vens.common.data

interface LocalDataSource<T> {

    fun readLocalStoreData(): List<T>?
}