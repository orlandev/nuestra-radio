package cu.ondev.nuestraradio.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.data.RadioRepository

class RadioBaseViewModel(radioBaseRepository: RadioRepository) : ViewModel() {

    val allRadioBase: LiveData<List<RadioBase>> = radioBaseRepository.allRadioBase.asLiveData()
}

class RadioBaseViewModelFactory(private val radioRepository: RadioRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RadioBaseViewModel::class.java)) {
            return RadioBaseViewModel(radioRepository) as T
        }
        throw IllegalArgumentException("unkknow ViewModel class")
    }
}