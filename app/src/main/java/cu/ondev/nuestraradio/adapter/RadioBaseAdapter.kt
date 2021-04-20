package cu.ondev.nuestraradio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import cu.ondev.nuestraradio.R
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.streamplayer.SimplePlayer

class RadioBaseAdapter(val radioBases: List<RadioBase>) :
    RecyclerView.Adapter<RadioBaseAdapter.ViewHolder>() {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val radioIcon = view.findViewById<ImageView>(R.id.radio_icon)
        val radioTitle = view.findViewById<TextView>(R.id.current_radio_name)
        val radioVisits = view.findViewById<TextView>(R.id.radio_visits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioBaseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioBaseAdapter.ViewHolder, position: Int) {
        val radioBase = radioBases[position]
        holder.radioTitle.text = radioBase.radioName
        holder.radioVisits.text = radioBase.visitas.toString()
        holder.view.setOnClickListener {
            val player = SimplePlayer.getPlayer(holder.view.context)
            val mediaItem = MediaItem.Builder().setUri(radioBase.radioStreamUrl).build()
            player.setMediaItem(mediaItem)
            player.playWhenReady = true
            player.seekTo(0)
            player.prepare()
        }
    }

    override fun getItemCount(): Int = radioBases.size
}