package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComponentAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val observeBikes = repository.observeBikes
    private val _currentBike: MutableStateFlow<Bike?> = MutableStateFlow(null)
    val currentBike: StateFlow<Bike?> = _currentBike

    val observeComponents = repository.observeComponents
    private val _currentParentComponent: MutableStateFlow<Component?> = MutableStateFlow(null)
    val currentParentComponent: StateFlow<Component?> = _currentParentComponent

    fun updateAttachedBike(bikeUid: Long?) {
        if (bikeUid != null) {
            viewModelScope.launch {
                _currentBike.value = repository.getBikeByUid(bikeUid)
            }
        } else {
            _currentBike.value = null
        }
    }

    fun updateParentComponent(componentUid: Long?) {
        if (componentUid != null) {
            viewModelScope.launch {
                _currentParentComponent.value = repository.getComponentByUid(componentUid)
            }
        } else {
            _currentParentComponent.value = null
        }
    }

    fun saveNewComponent(component: Component) {
        viewModelScope.launch {
            repository.insertComponent(component)
        }
    }
}