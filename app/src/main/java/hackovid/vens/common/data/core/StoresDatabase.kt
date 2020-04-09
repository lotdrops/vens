package hackovid.vens.common.data.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.StoreType
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val DATABASE_NAME = "stores-db"

@Database(
    entities = [Store::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoresDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile private var instance: StoresDatabase? = null

        fun getInstance(context: Context): StoresDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                StoresDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            getInstance(context).storeDao().insertList(buildMockData())
                        }
                    }
                })
                .build()

        private fun buildMockData() = (0..MOCK_SAMPLES).map { buildMockStore(it) }

        private fun buildMockStore(id: Int) = Store(
            id = id.toLong(),
            latitude = Random.nextDouble(LAT_MIN, LAT_MAX),
            longitude = Random.nextDouble(LONG_MIN, LONG_MAX),
            name = "Botiga $id",
            type = StoreType.values()[Random.nextInt(StoreType.values().size)],
            isFavourite = id.rem(100) == 0
        )
    }
}
private const val MOCK_SAMPLES = 1000

private const val LAT_MAX = 41.4382825
private const val LONG_MAX = 2.2066148
private const val LAT_MIN = 41.3670529
private const val LONG_MIN = 2.1269364
