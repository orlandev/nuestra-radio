package cu.ondev.nuestraradio.streamplayer

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer

object SimplePlayer {

  var player: SimpleExoPlayer? = null

  fun getPlayer(ctx: Context): SimpleExoPlayer {
    if (player == null) {
      player = SimpleExoPlayer.Builder(ctx).build()
    }
    return player as SimpleExoPlayer
  }
}
