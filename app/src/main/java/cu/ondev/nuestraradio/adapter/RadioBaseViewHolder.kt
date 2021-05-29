package cu.ondev.nuestraradio.adapter

import android.app.Activity
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import cu.ondev.nuestraradio.R
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.databinding.RadioItemsBinding
import cu.ondev.nuestraradio.services.SimplePlayer
import cu.ondev.nuestraradio.utilities.RadioPosterUrl

class RadioBaseViewHolder(
    private val binding: RadioItemsBinding,
    private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {
    private val radioIcon: ImageView = binding.radioIcon
    private val radioTitle: TextView = binding.currentRadioName
    private val radioVisits: TextView = binding.radioVisits

    fun bindData(radioBase: RadioBase, position: Int) {
        radioTitle.text = radioBase.radioName
        radioVisits.text = radioBase.visitas.toString()

        val radioPosterUrl = RadioPosterUrl.getRadioPoster(radioBase.radioName)

        imageLoader.loadImage(
            radioPosterUrl,
            radioIcon,
            R.drawable.radio_cuban_poster,
            R.drawable.radio_cuban_poster
        )
        binding.root.setOnClickListener {
            SimplePlayer.playAudio(
                (it.context) as Activity,
                position
            )
        }
    }
}