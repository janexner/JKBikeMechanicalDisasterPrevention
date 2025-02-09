package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
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

private const val TAG = "BikeDeleteVM"

@HiltViewModel(assistedFactory = BikeDeleteViewModel.BikeDeleteViewModelFactory::class)
class BikeDeleteViewModel @AssistedInject constructor(
    @Assisted val bikeUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _bike: MutableLiveData<Bike?> = MutableLiveData()
    val bike: LiveData<Bike?> = _bike

    private val _componentCount: MutableLiveData<Int> = MutableLiveData(0)
    val componentCount: LiveData<Int> = _componentCount

    private val _activityCount: MutableLiveData<Int> = MutableLiveData(0)
    val activityCount: LiveData<Int> = _activityCount

    init {
        // go look for the bike
        if (bikeUid > 0) {
            viewModelScope.launch {
                _bike.value = repository.getBikeByUid(bikeUid)
                if (bike.value != null) {
                    // find anything attached to the bike
                    _componentCount.value =
                        repository.getComponentCountByBike(bikeUid = bike.value!!.uid)
                    _activityCount.value =
                        repository.getActivityCountByBike(bikeUid = bike.value!!.uid)
                }
            }
        }
    }

    fun commitDelete(
        deleteAttachedComponents: Boolean = false,
        deleteAttachedActivities: Boolean = false,
    ) {
        Log.d(TAG, "About to delete bike $bikeUid...")
        if (bike.value != null) {
            Log.d(TAG, "Bike $bikeUid exists: ${bike.value!!.name}")
            if (deleteAttachedComponents) {
                Log.d(TAG, "Deleting components...")
                // delete components first
                viewModelScope.launch {
                    repository.deleteComponentsForBike(bike.value!!.uid)
                }
            }
            if (deleteAttachedActivities) {
                Log.d(TAG, "Deleting activities...")
                // delete activities first
                viewModelScope.launch {
                    val activities = repository.getActivitiesForBike(bike.value!!.uid)
                    activities.forEach { activity ->
                        Log.d(
                            TAG,
                            "  deleting activity ${activity.uid} / ${activity.title}"
                        )
                        repository.deleteActivityByUid(activity.uid)
                    }
                }
            }
            Log.d(TAG, "Now deleting bike itself...")
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