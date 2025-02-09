package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
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

private const val TAG = "ComponentDeleteVM"

@HiltViewModel(assistedFactory = ComponentDeleteViewModel.ComponentDeleteViewModelFactory::class)
class ComponentDeleteViewModel @AssistedInject constructor(
    @Assisted val componentUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _component: MutableLiveData<Component> = MutableLiveData()
    val component: LiveData<Component> = _component

    private val _componentCount: MutableLiveData<Int> = MutableLiveData(0)
    val componentCount: LiveData<Int> = _componentCount

    init {
        // load that component
        if (componentUid > 0) {
            viewModelScope.launch {
                _component.value = repository.getComponentByUid(componentUid)
                if (component.value != null) {
                    _componentCount.value = repository.getComponentCountByParent(componentUid)
                }
            }
        }
    }

    fun commitDelete(
        deleteAttachedComponents: Boolean = false
    ) {
        Log.d(TAG, "About to delete component $componentUid...")
        if (component.value != null) {
            Log.d(TAG, "Bike $componentUid exists: ${component.value!!.name}")
            if (deleteAttachedComponents) {
                Log.d(TAG, "Deleting attached components...")
                // delete components first
                viewModelScope.launch {
                    repository.deleteComponentsForParent(component.value!!.uid)
                }
            }
            Log.d(TAG, "Now deleting component itself...")
            // delete bike
            viewModelScope.launch {
                repository.deleteComponent(component.value!!)
            }
            Log.d(TAG, "Done deleting component.")
        }
    }

    @AssistedFactory
    interface ComponentDeleteViewModelFactory {
        fun create(componentUid: Long) : ComponentDeleteViewModel
    }
}