package cu.ondev.nuestraradio.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cu.ondev.nuestraradio.R
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.services.SimplePlayer

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
        holder.view.setOnClickListener {
            SimplePlayer.playAudio(
                (it.context) as Activity,
                position
            )
        }
    }

    override fun getItemCount(): Int = radioBases.size
}