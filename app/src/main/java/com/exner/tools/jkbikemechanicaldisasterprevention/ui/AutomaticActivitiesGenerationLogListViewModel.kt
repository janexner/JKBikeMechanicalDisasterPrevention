package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AutomaticActivitiesGenerationLogListViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val logEntries = repository.observeAutomaticActivitiesGenerationLog
}