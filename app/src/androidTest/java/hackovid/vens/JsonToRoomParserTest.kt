package hackovid.vens

import androidx.test.ext.junit.runners.AndroidJUnit4
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreType
import hackovid.vens.common.data.json.LocalJsonPersistency.Companion.localJsonStoreFile
import hackovid.vens.common.data.json.toStore
import kotlinx.coroutines.runBlocking

import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
open class JsonToRoomParserTest: BaseIntegrationTest() {

    private val largeJsonDataSizeInRows = 31628

    @Test
    fun test_that_read_json_large_data_is_correct_parsed_by_moshi() {
       var remoteStores = localDataSource.readLocalStoreData()
        assertEquals(remoteStores!!.size, largeJsonDataSizeInRows)

    }

}