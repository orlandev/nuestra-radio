package cu.ondev.nuestraradio.services

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

object SimplePlayer {
    const val Broadcast_PLAY_NEW_AUDIO = "cu.ondev.PlayNewAudio"


    var player: MediaPlayerService? = null
    var serviceBound = false

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

    fun playAudio(activity: Activity, mediaUrl: String) {
        if (!serviceBound) {
            val playerIntent = Intent(activity, MediaPlayerService::class.java)
            playerIntent.putExtra("media", mediaUrl)
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