package com.exner.tools.kjsbikemaintenancechecker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.generateTopLevelComponentsForNewBike
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AddBikeVM"

@HiltViewModel
class AddBikeViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    fun saveNewBike(bike: Bike, addComponents: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "Save ${bike.name} ${bike.buildDate} ${bike.mileage} ${bike.lastUsedDate}")
            val bikeUid = repository.insertBike(bike)
            Log.d(TAG, "Saved as uid $bikeUid.")

            if (addComponents) {
                Log.d(TAG, "Generating components for new bike...")
                generateTopLevelComponentsForNewBike(bikeUid, repository)
            }
        }
    }
}