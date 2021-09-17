package cu.ondev.nuestraradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import cu.ondev.nuestraradio.services.SimplePlayer
import cu.ondev.nuestraradio.ui.screens.NuestraRadio
import cu.ondev.nuestraradio.ui.theme.NuestraRadioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NuestraRadioTheme {
                // A surface container using the 'background' color from the theme
                NuestraRadio()
            }
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
