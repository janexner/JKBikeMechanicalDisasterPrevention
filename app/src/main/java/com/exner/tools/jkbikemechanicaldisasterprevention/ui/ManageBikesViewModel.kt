package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageBikesViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    val bikes = repository.observeBikes

}