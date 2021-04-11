package cu.ondev.nuestraradio.viewmodels

import androidx.lifecycle.*
import cu.ondev.nuestraradio.data.RadioBase
import cu.ondev.nuestraradio.data.RadioRepository
import kotlinx.coroutines.launch

class RadioBaseViewModel(private val radioBaseRepository: RadioRepository) : ViewModel() {

    val allRadioBase: LiveData<List<RadioBase>> = radioBaseRepository.allRadioBase.asLiveData()

    fun insertRadioBase(newRadioBase: RadioBase) = viewModelScope.launch {
        radioBaseRepository.insertRadioBase(newRadioBase)
    }

    fun updateRadioBase(newRadioBase: RadioBase) = viewModelScope.launch {
        radioBaseRepository.updateRadioBase(newRadioBase)
    }

    fun deleteRadioBase(newRadioBase: RadioBase) = viewModelScope.launch {
        radioBaseRepository.deleteRadioBase(newRadioBase)
    }
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