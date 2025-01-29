package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditBikeViewModel.BikeEditViewModelFactory::class)
class EditBikeViewModel @AssistedInject constructor(
    @Assisted val bikeUid: Long,
    repository: KJsRepository
) : ViewModel() {

    private val _bike: MutableLiveData<Bike?> = MutableLiveData()
    val bike: LiveData<Bike?> = _bike

    fun updateName(name: String) {
        if (bike.value != null) {
            _bike.value = bike.value!!.copy(
                name = name
            )
        }
    }



    init {
        if (bikeUid > 0) {
            viewModelScope.launch {
                _bike.value = repository.getBikeByUid(bikeUid)
            }
        }
    }

    @AssistedFactory
    interface BikeEditViewModelFactory {
        fun create(bikeUid: Long): EditBikeViewModel
    }
}
