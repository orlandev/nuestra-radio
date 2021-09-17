package cu.ondev.nuestraradio

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cu.ondev.nuestraradio.services.SimplePlayer


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NuestraRadio)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean("ServiceState", SimplePlayer.serviceBound)
        savedInstanceState.putInt("radioIndex", SimplePlayer.currentRadio)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        SimplePlayer.serviceBound = savedInstanceState.getBoolean("ServiceState")
        SimplePlayer.currentRadio = savedInstanceState.getInt("radioIndex")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (SimplePlayer.serviceBound) {
            unbindService(SimplePlayer.serviceConnection)
            //service is active
            SimplePlayer.player?.stopSelf()
        }
    }

}