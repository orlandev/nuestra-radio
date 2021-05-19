package cu.ondev.nuestraradio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import cu.ondev.nuestraradio.R
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.services.SimplePlayer
import cu.ondev.nuestraradio.utilities.RadioPosterUrl

class RadioBaseAdapter(private val radioBases: List<RadioBase>) :
    RecyclerView.Adapter<RadioBaseAdapter.ViewHolder>() {
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val radioIcon: ImageView = view.findViewById<ImageView>(R.id.radio_icon)
        val radioTitle: TextView = view.findViewById<TextView>(R.id.current_radio_name)
        val radioVisits: TextView = view.findViewById<TextView>(R.id.radio_visits)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioBaseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_items, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioBaseAdapter.ViewHolder, position: Int) {
        val radioBase = radioBases[position]
        holder.radioTitle.text = radioBase.radioName
        holder.radioVisits.text = radioBase.visitas.toString()

        val radioPosterUrl = RadioPosterUrl.getRadioPoster(radioBase.radioName)

        Glide.with(holder.itemView.context)
            .load(radioPosterUrl)
            .error(R.drawable.radio_cuban_poster)
            .placeholder(R.drawable.radio_cuban_poster)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.radioIcon)

        holder.view.setOnClickListener {
            SimplePlayer.playAudio(
                (it.context) as Activity,
                position
            )
        }
    }

    override fun getItemCount(): Int = radioBases.size
}