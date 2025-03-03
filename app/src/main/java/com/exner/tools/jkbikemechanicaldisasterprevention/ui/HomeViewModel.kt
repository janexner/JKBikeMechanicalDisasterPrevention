package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
import com.exner.tools.jkbikemechanicaldisasterprevention.scheduler.checkAndCreate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: KJsRepository
) : ViewModel() {

    val observeActivityWithBikeData = repository.observeActivityWithBikeDataOrderedByDueDate

    fun updateActivity(activity: Activity) {
        viewModelScope.launch {
            repository.updateActivity(activity)
        }
    }

    fun updateAttachedComponent(
        componentUid: Long,
        bikeUid: Long?
    ) {
        viewModelScope.launch {
            val bikeMileage = if (bikeUid != null) {
                repository.getBikeByUid(bikeUid)?.mileage ?: 0
            } else {
                0
            }
            val component = repository.getComponentByUid(componentUid)?.copy(
                lastCheckDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            )
            val newComponent = if (bikeMileage > 0) {
                component?.copy(
                    lastCheckMileage = bikeMileage
                )
            } else {
                component
            }
            if (newComponent != null) {
                repository.updateComponent(newComponent)
            }
        }
    }

    fun logComponentWearLevel(componentUid: Long, wearLevel: WearLevel) {
        viewModelScope.launch {
            val component = repository.getComponentByUid(componentUid)
            if (component != null) {
                val updatedComponent = component.copy(
                    wearLevel = wearLevel
                )
                repository.updateComponent(updatedComponent)
            }
        }
    }

    fun debugCheckAndCreate(context: Context) {
        checkAndCreate(context, repository)
    }
}
