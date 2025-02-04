package com.exner.tools.kjdoitnow.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.database.entities.Accessory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

private const val TAG = "AccessoryDeleteVM"

@HiltViewModel(assistedFactory = AccessoryDeleteViewModel.AccessoryDeleteViewModelFactory::class)
class AccessoryDeleteViewModel @AssistedInject constructor(
    @Assisted val accessoryUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _accessory: MutableLiveData<Accessory> = MutableLiveData()
    val accessory: LiveData<Accessory> = _accessory

    private val _accessoryCount: MutableLiveData<Int> = MutableLiveData(0)
    val accessoryCount: LiveData<Int> = _accessoryCount

    init {
        // load that accessory
        if (accessoryUid > 0) {
            viewModelScope.launch {
                _accessory.value = repository.getAccessoryByUid(accessoryUid)
                if (accessory.value != null) {
                    _accessoryCount.value = repository.getAccessoryCountByParent(accessoryUid)
                }
            }
        }
    }

    fun commitDelete(
        deleteAttachedComponents: Boolean = false
    ) {
        Log.d(TAG, "About to delete accessory $accessoryUid...")
        if (accessory.value != null) {
            Log.d(TAG, "Bike $accessoryUid exists: ${accessory.value!!.name}")
            if (deleteAttachedComponents) {
                Log.d(TAG, "Deleting attached accessories...")
                // delete components first
                viewModelScope.launch {
                    repository.deleteAccessoriesForParent(accessory.value!!.uid)
                }
            }
            Log.d(TAG, "Now deleting accessory itself...")
            // delete bike
            viewModelScope.launch {
                repository.deleteAccessory(accessory.value!!)
            }
            Log.d(TAG, "Done deleting accessory.")
        }
    }

    @AssistedFactory
    interface AccessoryDeleteViewModelFactory {
        fun create(accessoryUid: Long): AccessoryDeleteViewModel
    }
}