package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.database.entities.TemplateActivity
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = TemplateActivityEditViewModel.TemplateActivityEditViewModelFactory::class)
class TemplateActivityEditViewModel @AssistedInject constructor(
    @Assisted val templateActivityUid: Long,
    val repository: KJsRepository
) : ViewModel() {

    private val _templateActivity: MutableLiveData<TemplateActivity> = MutableLiveData()
    val templateActivity: LiveData<TemplateActivity> = _templateActivity

    init {
        if (templateActivityUid > 0) {
            viewModelScope.launch {
                _templateActivity.value = repository.getTemplateActivity(templateActivityUid)
            }
        }
    }

    fun updateTitle(title: String) {
        if (templateActivity.value != null) {
            _templateActivity.value = templateActivity.value!!.copy(
                title = title
            )
        }
    }

    fun updateDescription(description: String) {
        if (templateActivity.value != null) {
            _templateActivity.value = templateActivity.value!!.copy(
                description = description
            )
        }
    }

    fun updateIsEBikeSpecific(isEbikeSpecific: Boolean) {
        if (templateActivity.value != null) {
            _templateActivity.value = templateActivity.value!!.copy(
                isEBikeSpecific = isEbikeSpecific
            )
        }
    }

    fun updateRideLevel(rideLevel: RideLevel?) {
        if (templateActivity.value != null) {
            _templateActivity.value = templateActivity.value!!.copy(
                rideLevel = rideLevel
            )
        }
    }

    fun commitActivity() {
        if (templateActivity.value != null) {
            viewModelScope.launch {
                repository.updateTemplateActivity(templateActivity.value!!)
            }
        }
    }

    @AssistedFactory
    interface TemplateActivityEditViewModelFactory {
        fun create(templateActivityUid: Long): TemplateActivityEditViewModel
    }
}