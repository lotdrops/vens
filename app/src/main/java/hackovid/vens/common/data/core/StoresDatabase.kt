package hackovid.vens.common.data.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import hackovid.vens.common.data.Favourite
import hackovid.vens.common.data.FavouriteDao
import hackovid.vens.common.data.Store
import hackovid.vens.common.data.StoreDao

const val DATABASE_NAME = "stores-db"

@Database(
    entities = [Store::class, Favourite::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StoresDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
    abstract fun favouriteDao(): FavouriteDao

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
