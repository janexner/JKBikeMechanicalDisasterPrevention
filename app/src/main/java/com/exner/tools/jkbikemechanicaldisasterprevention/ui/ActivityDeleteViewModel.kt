package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

private const val TAG = "ActivityDeleteVM"

@HiltViewModel(assistedFactory = ActivityDeleteViewModel.ActivityDeleteViewModelFactory::class)
class ActivityDeleteViewModel @AssistedInject constructor(
    @Assisted val activityUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _activity: MutableLiveData<Activity> = MutableLiveData()
    val activity: LiveData<Activity> = _activity

    init {
        // go look for the activity
        if (activityUid > 0) {
            viewModelScope.launch {
                _activity.value = repository.getActivityByUid(activityUid)
            }
        }
    }

    fun commitDelete() {
        Log.d(TAG, "About to delete activity $activityUid...")
        if (activity.value != null) {
            Log.d(TAG, "Activity $activityUid exists: ${activity.value!!.title}")
            viewModelScope.launch {
                repository.deleteActivityByUid(activityUid)
            }
        }
    }

    @AssistedFactory
    interface ActivityDeleteViewModelFactory {
        fun create(activityUid: Long) : ActivityDeleteViewModel
    }
}