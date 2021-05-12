package cu.ondev.nuestraradio.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.media.session.MediaSessionManager
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import cu.ondev.nuestraradio.R
import java.io.IOException


class MediaPlayerService : Service(), OnCompletionListener, OnPreparedListener,
    OnErrorListener, OnSeekCompleteListener, OnInfoListener,
    OnBufferingUpdateListener, OnAudioFocusChangeListener {
    // Binder given to clients
    val ACTION_PLAY = "cu.ondev.audioplayer.ACTION_PLAY"
    val ACTION_PAUSE = "cu.ondev.audioplayer.ACTION_PAUSE"
    val ACTION_PREVIOUS = "cu.ondev.audioplayer.ACTION_PREVIOUS"
    val ACTION_NEXT = "cu.ondev.audioplayer.ACTION_NEXT"
    val ACTION_STOP = "cu.ondev.audioplayer.ACTION_STOP"


    //MediaSession
    private var mediaSessionManager: MediaSessionManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var transportControls: MediaControllerCompat.TransportControls? = null
    private val NOTIFICATION_ID = 101
    private var ongoingCall = false
    private var phoneStateListener: PhoneStateListener? = null
    private var telephonyManager: TelephonyManager? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private var resumePosition = 0
    override fun onCreate() {
        super.onCreate()
        // Perform one-time setup procedures
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener()
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver()
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio()
    }

    //Handle incoming phone calls
    private fun callStateListener() {
        // Get the telephony manager
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        //Starting listening for PhoneState changes
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                when (state) {
                    TelephonyManager.CALL_STATE_OFFHOOK, TelephonyManager.CALL_STATE_RINGING -> if (mediaPlayer != null) {
                        pauseMedia()
                        ongoingCall = true
                    }
                    TelephonyManager.CALL_STATE_IDLE ->                   // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false
                                resumeMedia()
                            }
                        }
                }
            }
        }
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager!!.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            stopMedia()
            mediaPlayer!!.release()
        }
        removeAudioFocus()
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }
        removeNotification()

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver)
        unregisterReceiver(playNewAudio)

        //clear cached playlist
        // StorageUtil(applicationContext).clearCachedAudioPlaylist()
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.setOnErrorListener(this)
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnBufferingUpdateListener(this)
        mediaPlayer?.setOnSeekCompleteListener(this)
        mediaPlayer?.setOnInfoListener(this)
        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer?.reset()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK);

        try {
            // Set the data source to the mediaFile location
            mediaPlayer?.setDataSource(SimplePlayer.getRadioBaseByIndex(SimplePlayer.currentRadio).radioStreamUrl)
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        }
        mediaPlayer?.prepareAsync()

    }


    private val iBinder: IBinder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder {
        return iBinder
    }

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    override fun onCompletion(mp: MediaPlayer) {
        //Invoked when playback of a media source has completed.
        stopMedia()
        //stop the service
        stopSelf()
    }

    //Handle errors
    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        //Invoked when there has been an error during an asynchronous operation
        when (what) {
            MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
            )
            MEDIA_ERROR_SERVER_DIED -> Log.d(
                "MediaPlayer Error",
                "MEDIA ERROR SERVER DIED $extra"
            )
            MEDIA_ERROR_UNKNOWN -> Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN $extra")
        }
        return false
    }

    override fun onInfo(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        //Invoked to communicate some info.
        return false
    }

    override fun onPrepared(mp: MediaPlayer) {
        playMedia()
    }

    override fun onAudioFocusChange(focusState: Int) {
        //Invoked when the audio focus of the system is updated.
        when (focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                // resume playback
                if (mediaPlayer == null) initMediaPlayer() else if (!mediaPlayer?.isPlaying!!) mediaPlayer?.start()
                mediaPlayer?.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ->             // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->             // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer?.isPlaying == true) mediaPlayer?.setVolume(0.1f, 0.1f)
        }
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = audioManager?.requestAudioFocus(
            this,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun removeAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager?.abandonAudioFocus(this)
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    private fun playMedia() {
        if (!mediaPlayer?.isPlaying!!) {
            mediaPlayer?.start()
        }
    }

    private fun stopMedia() {
        if (mediaPlayer == null) return
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
    }

    private fun pauseMedia() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            resumePosition = mediaPlayer?.currentPosition!!
        }
    }

    private fun resumeMedia() {
        if (!mediaPlayer?.isPlaying!!) {
            mediaPlayer?.seekTo(resumePosition)
            mediaPlayer?.start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Request audio focus
        if (!requestAudioFocus()) {
            //Could not gain focus
            stopSelf()
        }
        if (mediaSessionManager == null) {
            try {
                initMediaSession()
                initMediaPlayer()
            } catch (e: RemoteException) {
                e.printStackTrace()
                stopSelf()
            }
            buildNotification(PlaybackStatus.PLAYING)
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent)
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY_COMPATIBILITY;
    }

    override fun onSeekComplete(mp: MediaPlayer?) {

    }

    private fun handleIncomingActions(playbackAction: Intent?) {
        if (playbackAction == null || playbackAction.action == null) return
        val actionString = playbackAction.action
        when {
            actionString.equals(ACTION_PLAY, ignoreCase = true) -> {
                transportControls!!.play()
            }
            actionString.equals(ACTION_PAUSE, ignoreCase = true) -> {
                transportControls!!.pause()
            }
            actionString.equals(ACTION_NEXT, ignoreCase = true) -> {
                transportControls!!.skipToNext()
            }
            actionString.equals(ACTION_PREVIOUS, ignoreCase = true) -> {
                transportControls!!.skipToPrevious()
            }
            actionString.equals(ACTION_STOP, ignoreCase = true) -> {
                transportControls!!.stop()
            }
        }
    }

    //Becoming noisy
    private val becomingNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia()
            buildNotification(PlaybackStatus.PAUSED)
        }
    }

    private fun registerBecomingNoisyReceiver() {
        //register after getting audio focus
        val intentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(becomingNoisyReceiver, intentFilter)
    }

    private val playNewAudio: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Get the new media index form SharedPreferences

            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio
            stopMedia()
            mediaPlayer?.reset()
            initMediaPlayer()
            updateMetaData()
            buildNotification(PlaybackStatus.PLAYING)
        }
    }

    private fun register_playNewAudio() {
        //Register playNewMedia receiver
        val filter = IntentFilter(SimplePlayer.Broadcast_PLAY_NEW_AUDIO)
        registerReceiver(playNewAudio, filter)
    }

    @Throws(RemoteException::class)
    private fun initMediaSession() {
        if (mediaSessionManager != null) return  //mediaSessionManager exists
        mediaSessionManager = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager
        // Create a new MediaSession
        mediaSession = MediaSessionCompat(applicationContext, "NuestraRadioPlayer")
        //Get MediaSessions transport controls
        transportControls = mediaSession!!.controller.transportControls
        //set MediaSession -> ready to receive media commands
        mediaSession!!.isActive = true
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        //Set mediaSession's MetaData
        updateMetaData()

        // Attach Callback to receive MediaSession updates
        mediaSession!!.setCallback(object : MediaSessionCompat.Callback() {
            // Implement callbacks
            override fun onPlay() {
                super.onPlay()
                resumeMedia()
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onPause() {
                super.onPause()
                pauseMedia()
                buildNotification(PlaybackStatus.PAUSED)
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                //   skipToNext()
                updateMetaData()
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                //skipToPrevious()
                updateMetaData()
                buildNotification(PlaybackStatus.PLAYING)
            }

            override fun onStop() {
                super.onStop()
                removeNotification()
                //Stop the service
                stopSelf()
            }

            override fun onSeekTo(position: Long) {
                super.onSeekTo(position)
            }
        })
    }

    private fun updateMetaData() {
        val albumArt = BitmapFactory.decodeResource(
            resources,
            R.drawable.common_full_open_on_phone
        ) //replace with medias albumArt
        // Update the current metadata
        var radioBase = SimplePlayer.getRadioBaseByIndex(SimplePlayer.currentRadio)
        mediaSession!!.setMetadata(
            MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, radioBase.radioName)
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ALBUM,
                    "Visitas: ${radioBase.visitas}"
                )
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, radioBase.radioName)
                .build()
        )
    }

    private fun buildNotification(playbackStatus: PlaybackStatus) {
        var notificationAction = R.drawable.ic_round_pause_24 //needs to be initialized
        var play_pauseAction: PendingIntent? = null

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus === PlaybackStatus.PLAYING) {
            notificationAction = R.drawable.ic_round_pause_24
            //create the pause action
            play_pauseAction = playbackAction(1)
        } else if (playbackStatus === PlaybackStatus.PAUSED) {
            notificationAction = R.drawable.ic_round_play_arrow_24
            //create the play action
            play_pauseAction = playbackAction(0)
        }
        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_round_play_arrow_24
        ) //replace with your own image

        // Create a new Notification
        createNotificationChannel()
        var radioBase = SimplePlayer.getRadioBaseByIndex(SimplePlayer.currentRadio)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setShowWhen(false) // Set the Notification style
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle() // Attach our MediaSession token
                        .setMediaSession(mediaSession!!.sessionToken) // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2)
                ) // Set the Notification color
                .setColor(resources.getColor(R.color.design_default_color_primary)) // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_round_play_arrow_24) // Set Notification content information
                .setContentText(radioBase.radioName)
                .setContentTitle("Visitas: ${radioBase.visitas}")
                .setContentInfo(radioBase.radioName) // Add playback actions
                .addAction(R.drawable.ic_round_skip_previous_24, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(
                    R.drawable.ic_round_skip_next_24,
                    "next",
                    playbackAction(2)
                ) as NotificationCompat.Builder
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    val CHANNEL_ID = "NUESTRA_RADIO_CHANNEL_ID"

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun playbackAction(actionNumber: Int): PendingIntent? {
        val playbackAction = Intent(this, MediaPlayerService::class.java)
        when (actionNumber) {
            0 -> {
                // Play
                playbackAction.action = ACTION_PLAY
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            1 -> {
                // Pause
                playbackAction.action = ACTION_PAUSE
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            2 -> {
                // Next track
                playbackAction.action = ACTION_NEXT
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            3 -> {
                // Previous track
                playbackAction.action = ACTION_PREVIOUS
                return PendingIntent.getService(this, actionNumber, playbackAction, 0)
            }
            else -> {
            }
        }
        return null
    }

    private fun removeNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }


}