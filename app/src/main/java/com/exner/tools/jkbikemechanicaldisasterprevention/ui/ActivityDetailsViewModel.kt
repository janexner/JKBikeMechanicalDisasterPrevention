package com.exner.tools.jkbikemechanicaldisasterprevention.ui

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

@HiltViewModel(assistedFactory = ActivityDetailsViewModel.ActivityDetailsViewModelFactory::class)
class ActivityDetailsViewModel @AssistedInject constructor(
    @Assisted val activityUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _activity: MutableLiveData<Activity> = MutableLiveData()
    val activity: LiveData<Activity> = _activity

    init {
        if (activityUid > 0) {
            viewModelScope.launch {
                _activity.value = repository.getActivityByUid(activityUid)
            }
        }
    }

    @AssistedFactory
    interface ActivityDetailsViewModelFactory {
        fun create(activityUid: Long): ActivityDetailsViewModel
    }
}