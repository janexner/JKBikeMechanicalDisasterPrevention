package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userPreferencesManager: UserPreferencesManager,
    repository: KJsRepository
) : ViewModel() {
    val observeActivitiesByDueDate = repository.observeActivitiesByDueDate
}