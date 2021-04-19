package cu.ondev.nuestraradio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cu.ondev.nuestraradio.RadioAplication
import cu.ondev.nuestraradio.adapter.RadioBaseAdapter
import cu.ondev.nuestraradio.databinding.FragmentRadioListBinding
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModel
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RadioListFragment : Fragment() {
    lateinit var binding: FragmentRadioListBinding
    private val radioBasesViewModel: RadioBaseViewModel by viewModels {
        RadioBaseViewModelFactory((requireActivity().application as RadioAplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRadioListBinding.inflate(inflater, container, false)

        radioBasesViewModel.allRadioBase.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.radioList.adapter = RadioBaseAdapter(it)
                /*for (radio in it) {
                    val title = radio.radioName
                    val streamUrl = radio.radioStreamUrl
                    val visto = radio.visitas
                    Log.d("URL-DATA", "newRadioBase: $title | $streamUrl | $visto")
                }*/
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    (requireActivity().application as RadioAplication).repository.updateDataBase()
                }
            }
        })



        return binding.root
    }
}