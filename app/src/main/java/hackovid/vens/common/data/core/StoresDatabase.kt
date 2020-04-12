package hackovid.vens.common.data.core

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao
import hackovid.vens.common.data.json.LocalJsonPersistency
import hackovid.vens.common.data.json.MoshiFactory
import hackovid.vens.common.data.json.toStore
import hackovid.vens.common.utils.FileReaderUtilities
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
                    }
                })
                .build()
    }
}