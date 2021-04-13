package cu.ondev.nuestraradio.data

import androidx.annotation.WorkerThread
import cu.ondev.nuestraradio.api.RadioApi
import kotlinx.coroutines.flow.Flow

class RadioRepository(private val radioDao: RadioBaseDao, private val radioApi: RadioApi) {
    val allRadioBase: Flow<List<RadioBase>> = radioDao.getAllRadioBases()

    @WorkerThread
    suspend fun updateDataBase() {
        radioApi.getRadiosStreamData()
    }

    @WorkerThread
    suspend fun insertRadioBase(newRadio: RadioBase) {
        radioDao.insertRadioBase(newRadio)
    }

    @WorkerThread
    suspend fun updateRadioBase(newRadio: RadioBase) {
        radioDao.updateRadioBase(newRadio)
    }

    @WorkerThread
    suspend fun deleteRadioBase(deleteRadio: RadioBase) {
        radioDao.updateRadioBase(deleteRadio)
    }
}