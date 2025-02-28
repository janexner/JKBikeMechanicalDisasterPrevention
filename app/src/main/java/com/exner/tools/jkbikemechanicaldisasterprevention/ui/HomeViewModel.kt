package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.WearLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
}
