package hackovid.vens

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import hackovid.vens.common.data.LocalDataSource
import hackovid.vens.common.data.core.StoresDatabase
import hackovid.vens.common.data.json.LocalJsonPersistency
import hackovid.vens.common.data.json.MoshiFactory
import hackovid.vens.common.data.json.RemoteStore
import hackovid.vens.common.utils.FileReaderUtilities
import java.io.IOException
import org.junit.After
import org.junit.Before

abstract class BaseIntegrationTest {

    protected lateinit var appDatabase: StoresDatabase
    protected lateinit var jsonReaderUtilities: FileReaderUtilities
    protected lateinit var localDataSource: LocalDataSource<RemoteStore>
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun initDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            context,
            StoresDatabase::class.java)
            .build()

        jsonReaderUtilities = FileReaderUtilities(context)
        localDataSource = LocalJsonPersistency(fileReader = jsonReaderUtilities,
            moshi = MoshiFactory.getInstance()
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}
