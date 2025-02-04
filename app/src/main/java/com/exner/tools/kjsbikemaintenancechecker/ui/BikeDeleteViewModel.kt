package com.exner.tools.kjsbikemaintenancechecker.ui

import android.util.Log
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

private const val TAG = "BikeDeleteVM"

@HiltViewModel(assistedFactory = BikeDeleteViewModel.BikeDeleteViewModelFactory::class)
class BikeDeleteViewModel @AssistedInject constructor(
    @Assisted val bikeUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _bike: MutableLiveData<Bike?> = MutableLiveData()
    val bike: LiveData<Bike?> = _bike

    init {
        // go look for the bike
        if (bikeUid > 0) {
            viewModelScope.launch {
                _bike.value = repository.getBikeByUid(bikeUid)
            }
        }
    }

    fun commitDelete() {
        Log.d(TAG, "About to delete bike $bikeUid...")
        if (bike.value != null) {
            Log.d(TAG, "Bike $bikeUid exists: ${bike.value!!.name}")
            Log.d(TAG, "Deleting bike...")
            // delete bike
            viewModelScope.launch {
                repository.deleteBike(bike.value!!)
            }
            Log.d(TAG, "Done deleting bike.")
        }
    }


    @AssistedFactory
    interface BikeDeleteViewModelFactory {
        fun create(bikeUid: Long): BikeDeleteViewModel
    }
}