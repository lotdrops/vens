package hackovid.vens.common.data

interface LocalDataSource<T> {

    fun readLocalStoreData(): List<T>?
}

interface LocalStorage {

    fun shouldBeDisplayedOnBoardScreen():Boolean
    fun setOnboardScreenVisibility(shouldBeDisplayed: Boolean)

}