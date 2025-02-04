package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjdoitnow.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageBikesViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    val bikes = repository.observeBikes

}