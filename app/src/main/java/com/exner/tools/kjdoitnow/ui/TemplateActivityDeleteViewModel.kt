package com.exner.tools.kjdoitnow.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.database.entities.TemplateActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

private const val TAG = "TemplateActivityDeleteVM"

@HiltViewModel(assistedFactory = TemplateActivityDeleteViewModel.TemplateActivityDeleteViewModelFactory::class)
class TemplateActivityDeleteViewModel @AssistedInject constructor(
    @Assisted val templateActivityUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _activity: MutableLiveData<TemplateActivity> = MutableLiveData()
    val activity: LiveData<TemplateActivity> = _activity

    init {
        // go look for the activity
        if (templateActivityUid > 0) {
            viewModelScope.launch {
                _activity.value = repository.getTemplateActivity(templateActivityUid)
            }
        }
    }

    fun commitDelete() {
        Log.d(TAG, "About to delete activity $templateActivityUid...")
        if (activity.value != null) {
            Log.d(TAG, "Activity $templateActivityUid exists: ${activity.value!!.title}")
            viewModelScope.launch {
                repository.deleteTemplateActivityByUid(templateActivityUid)
            }
        }
    }

    @AssistedFactory
    interface TemplateActivityDeleteViewModelFactory {
        fun create(templateActivityUid: Long): TemplateActivityDeleteViewModel
    }
}