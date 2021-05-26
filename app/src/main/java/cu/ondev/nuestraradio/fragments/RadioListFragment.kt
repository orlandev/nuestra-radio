package cu.ondev.nuestraradio.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import cu.ondev.nuestraradio.RadioAplication
import cu.ondev.nuestraradio.adapter.RadioBaseAdapter
import cu.ondev.nuestraradio.data.RadioRepository
import cu.ondev.nuestraradio.databinding.FragmentRadioListBinding
import cu.ondev.nuestraradio.services.SimplePlayer
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModel
import cu.ondev.nuestraradio.viewmodels.RadioBaseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RadioListFragment : Fragment() {
    private lateinit var binding: FragmentRadioListBinding
    private val radioBasesViewModel: RadioBaseViewModel by viewModels {
        RadioBaseViewModelFactory((requireActivity().application as RadioAplication).repository)
    }
    private val radioBaseRepository: RadioRepository by lazy { radioBasesViewModel.getRepository() }
    private val radioBaseListAdapter: RadioBaseAdapter by lazy {
        RadioBaseAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRadioListBinding.inflate(inflater, container, false)

        binding.radioList.adapter = radioBaseListAdapter
        binding.swipeContainer.setOnRefreshListener {
            updateRadioDataBase()
        }

        radioBasesViewModel.allRadioBase.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                SimplePlayer.allRadio = it
                radioBaseListAdapter.setData(it)
            } else {
                updateRadioDataBase()
            }
        })
        return binding.root
    }

    fun updateRadioDataBase() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("TAG", "onCreateView: Call to update url")
                withContext(Dispatchers.Main) {
                    binding.swipeContainer.isRefreshing = true
                }
                radioBaseRepository.updateDataBase()
            } catch (ex: Exception) {
                Log.d("TAG", "onCreateView: ERROR Call to update url: ${ex.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.swipeContainer.isRefreshing = false
                    Toast.makeText(context, "${ex.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.swipeContainer.isRefreshing = false
                }
            }
        }
    }

}