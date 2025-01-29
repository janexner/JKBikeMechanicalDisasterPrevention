package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ComponentEditViewModel.ComponentEditViewModelFactory::class)
class ComponentEditViewModel @AssistedInject constructor(
    @Assisted val componentUid: Long,
    repository: KJsRepository
) : ViewModel() {

    private val _component: MutableLiveData<Component?> = MutableLiveData()
    val component: LiveData<Component?> = _component

    init {
        if (componentUid > 0) {
            viewModelScope.launch {
                _component.value = repository.getComponentByUid(componentUid)
            }
        }
    }

    @AssistedFactory
    interface ComponentEditViewModelFactory {
        fun create(componentUid: Long): ComponentEditViewModel
    }
}