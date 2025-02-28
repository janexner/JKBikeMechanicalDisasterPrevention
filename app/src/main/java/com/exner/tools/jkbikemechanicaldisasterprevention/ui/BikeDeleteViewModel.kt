package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BikeDeleteViewModel.BikeDeleteViewModelFactory::class)
class BikeDeleteViewModel @AssistedInject constructor(
    @Assisted val bikeUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _bike: MutableLiveData<Bike?> = MutableLiveData()
    val bike: LiveData<Bike?> = _bike

    fun commitDelete() {
        if (bike.value != null) {
            // delete bike
            viewModelScope.launch {
                repository.deleteBike(bike.value!!)
            }
        }
    }

    init {
        // go look for the bike
        if (bikeUid > 0) {
            viewModelScope.launch {
                _bike.value = repository.getBikeByUid(bikeUid)
            }
        }
    }

    @AssistedFactory
    interface BikeDeleteViewModelFactory {
        fun create(bikeUid: Long): BikeDeleteViewModel
    }
}