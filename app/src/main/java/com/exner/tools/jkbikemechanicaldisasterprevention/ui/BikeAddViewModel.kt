package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BikeAddVM"

@HiltViewModel
class BikeAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    fun saveNewBike(bike: Bike) {
        viewModelScope.launch {
            Log.d(TAG, "Save ${bike.name} ${bike.buildDate} ${bike.mileage} ${bike.lastUsedDate}")
            val bikeUid = repository.insertBike(bike)
            Log.d(TAG, "Saved as uid $bikeUid.")
        }
    }
}