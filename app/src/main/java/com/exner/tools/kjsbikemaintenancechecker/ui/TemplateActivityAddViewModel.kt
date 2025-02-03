package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.database.entities.TemplateActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateActivityAddViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    fun saveTemplateActivity(templateActivity: TemplateActivity) {
        viewModelScope.launch {
            repository.insertTemplateActivity(templateActivity)
        }
    }
}