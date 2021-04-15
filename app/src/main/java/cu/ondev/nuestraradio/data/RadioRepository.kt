package cu.ondev.nuestraradio.data

import cu.ondev.nuestraradio.api.RadioApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count

class RadioRepository(private val radioDao: RadioBaseDao) {
    val allRadioBase: Flow<List<RadioBase>> = radioDao.getAllRadioBases()

    suspend fun updateDataBase() {
        val newRadio = RadioApi.getRadiosStreamData()
        if (!newRadio.isNullOrEmpty()) {
            for (itemRadio in newRadio) {
                radioDao.insertRadioBase(itemRadio)
            }
        }
    }


}