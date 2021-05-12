package cu.ondev.nuestraradio

import android.app.Application
import cu.ondev.nuestraradio.data.RadioDatabase
import cu.ondev.nuestraradio.data.RadioRepository

class RadioAplication : Application() {
    private val database by lazy { RadioDatabase.getDataBase(this) }
    val repository by lazy { RadioRepository(database.radioBaseDao()) }
}