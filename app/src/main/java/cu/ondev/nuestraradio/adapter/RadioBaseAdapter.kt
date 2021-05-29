package cu.ondev.nuestraradio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.databinding.RadioItemsBinding

class RadioBaseAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<RadioBaseViewHolder>() {

    private val radioBases = mutableListOf<RadioBase>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioBaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RadioItemsBinding.inflate(inflater, parent, false)

        return RadioBaseViewHolder(binding, imageLoader = imageLoader)
    }

    override fun onBindViewHolder(holder: RadioBaseViewHolder, position: Int) {
        val radioBase = radioBases[position]
        holder.bindData(radioBase, position)
    }

    fun setData(newList: List<RadioBase>) {
        radioBases.clear()
        radioBases.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = radioBases.size
}