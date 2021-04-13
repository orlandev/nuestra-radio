package cu.ondev.nuestraradio

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.data.RadioBaseDao
import cu.ondev.nuestraradio.data.RadioDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RadioDataBaseTest {

    private lateinit var context: Context
    private lateinit var db: RadioDatabase
    private lateinit var radioDao: RadioBaseDao

    @Before
    fun setupTest() {
        context = ApplicationProvider.getApplicationContext<Context>()
        assertNotNull(context)
    }

    @Test
    fun createDataBase() {
        // Context of the app under test.
        db = RadioDatabase.getDataBase(context)
        assertNotNull("Creating DataBase", db)
        radioDao = db.radioBaseDao()
        assertNotNull("Getting a correspondient DAO", radioDao)
        assertEquals("Opening DATBASE", true, db.isOpen)
    }

    @Test
    suspend fun addRadioBaseTest() {
        val newRadio = RadioBase(0, "Radio Rebelde", "https://asdasd.asdasd.de/wedwef", 34)
        assertNotNull("Creating a new Radio Base", newRadio)
        try {
            radioDao.insertRadioBase(newRadio)
        } catch (ex: Exception) {
            assertNotNull("Error al insertar datos en la DB", ex)
        }
    }
}