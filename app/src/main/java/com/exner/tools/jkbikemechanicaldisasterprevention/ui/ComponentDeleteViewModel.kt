package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ComponentDeleteViewModel.ComponentDeleteViewModelFactory::class)
class ComponentDeleteViewModel @AssistedInject constructor(
    @Assisted val componentUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _component: MutableLiveData<Component?> = MutableLiveData()
    val component: LiveData<Component?> = _component

    fun commitDelete() {
        if (component.value != null) {
            viewModelScope.launch {
                repository.deleteComponent(component.value!!)
            }
        }
    }

    init {
        if (componentUid > 0) {
            viewModelScope.launch {
                _component.value = repository.getComponentByUid(componentUid)
            }
        }
    }

    @AssistedFactory
    interface ComponentDeleteViewModelFactory {
        fun create(componentUid: Long): ComponentDeleteViewModel
    }
}