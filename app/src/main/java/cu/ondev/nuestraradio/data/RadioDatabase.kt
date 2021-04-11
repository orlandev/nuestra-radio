package cu.ondev.nuestraradio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cu.ondev.nuestraradio.utilities.DATABASE_NAME

@Database(entities = [RadioBase::class], version = 1)
abstract class RadioDatabase : RoomDatabase() {
    abstract fun radioBaseDao(): RadioBaseDao

    companion object {
        @Volatile
        private var INSTANCE: RadioDatabase? = null
        fun getDataBase(ctx: Context): RadioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        ctx.applicationContext,
                        RadioDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }

}