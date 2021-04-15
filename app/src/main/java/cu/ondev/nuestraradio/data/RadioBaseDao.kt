package cu.ondev.nuestraradio.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRadioBase(vararg newRadio: RadioBase)

    @Update
    suspend fun updateRadioBase(vararg newRadio: RadioBase)

    @Delete
    suspend fun deleteRadioBase(vararg deleteRadio: RadioBase)

    @Query("SELECT * FROM radiobases ORDER BY visitas DESC")
    fun getAllRadioBases(): Flow<List<RadioBase>>

    @Query("SELECT * FROM radiobases ORDER BY id LIMIT 1")
    suspend fun isDbEmpty(): List<RadioBase>
}