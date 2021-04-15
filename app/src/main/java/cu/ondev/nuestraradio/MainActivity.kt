package cu.ondev.nuestraradio

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModel
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val radioBasesViewModel: RadioBaseViewModel by viewModels {
        RadioBaseViewModelFactory((application as RadioAplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        radioBasesViewModel.allRadioBase.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                for (radio in it) {
                    val title = radio.radioName
                    val streamUrl = radio.radioStreamUrl
                    val visto = radio.visitas
                    Log.d("URL-DATA", "newRadioBase: $title | $streamUrl | $visto")
                }

                //TODO Update Recycle list
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    (application as RadioAplication).repository.updateDataBase()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}