package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class KJsGlobalScaffoldViewModel @Inject constructor(

) : ViewModel() {

    private val _destinationTitle: MutableStateFlow<String> = MutableStateFlow("")
    val destinationTitle: StateFlow<String> = _destinationTitle

    fun setDestinationTitle(newTitle: String) {
        _destinationTitle.value = newTitle
    }
}