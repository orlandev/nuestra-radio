package cu.ondev.nuestraradio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRadioBase(newRadio: RadioBase)

    @Query("DELETE FROM radiobases")
    suspend fun deleteRadioBase()

    @Query("SELECT * FROM radiobases ORDER BY visitas DESC")
    fun getAllRadioBases(): Flow<List<RadioBase>>

    @Query("SELECT * FROM radiobases ORDER BY id LIMIT 1")
    suspend fun isDbEmpty(): List<RadioBase>
}