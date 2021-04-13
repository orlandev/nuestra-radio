package cu.ondev.nuestraradio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import cu.ondev.nuestraradio.utilities.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class RadioBase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var radioName: String,
    var radioStreamUrl: String,
    var visitas: Int
)