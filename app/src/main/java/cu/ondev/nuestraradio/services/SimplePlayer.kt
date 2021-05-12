package cu.ondev.nuestraradio.services

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import cu.ondev.nuestraradio.data.RadioBase

object SimplePlayer {
    const val Broadcast_PLAY_NEW_AUDIO = "cu.ondev.PlayNewAudio"
    var player: MediaPlayerService? = null
    var serviceBound = false
    var currentRadio: Int = 0
    var allRadio: List<RadioBase>? = null


    val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }

    fun getRadioBaseByIndex(index: Int): RadioBase {
        return allRadio!![index]
    }

    fun playAudio(activity: Activity, radioIndex: Int) {
        if (!serviceBound) {
            val playerIntent = Intent(activity, MediaPlayerService::class.java)
            currentRadio = radioIndex
            activity.startService(playerIntent)
            activity.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            //Service is active
            //Send media with BroadcastReceiver
            val broadcastIntent = Intent(Broadcast_PLAY_NEW_AUDIO)
            activity.sendBroadcast(broadcastIntent)
        }
    }

}