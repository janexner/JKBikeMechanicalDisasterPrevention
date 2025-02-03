package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjsbikemaintenancechecker.database.KJsRepository
import com.exner.tools.kjsbikemaintenancechecker.ui.helpers.RideLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ManageTemplateActivitiesViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    val rideLevel: StateFlow<List<RideLevel>> = MutableStateFlow(RideLevel.getListOfRideLevels())

    val templateActivities = repository.observeTemplateActivity

}