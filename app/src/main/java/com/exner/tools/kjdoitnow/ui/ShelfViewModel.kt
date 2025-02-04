package com.exner.tools.kjdoitnow.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.kjdoitnow.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShelfViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    val shelvedComponents = repository.observeShelvedComponents

}