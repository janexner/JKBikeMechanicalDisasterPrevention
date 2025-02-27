package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComponentAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val allBikes = repository.observeBikes

    fun saveNewComponent(component: Component) {
        viewModelScope.launch {
            repository.insertComponent(component)
        }
    }
}