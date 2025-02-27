package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ComponentAnalysisViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    val retiredComponents = repository.observeRetiredComponents

    private val _listOfComponentUidsForAnalysis: MutableStateFlow<List<Long>> = MutableStateFlow(
        emptyList()
    )
    val listOfComponentUidsForAnalysis: StateFlow<List<Long>> = _listOfComponentUidsForAnalysis

    fun addUidToList(uid: Long) {
        val tempList = listOfComponentUidsForAnalysis.value.toMutableList()
        tempList.add(uid)
        _listOfComponentUidsForAnalysis.value = tempList
    }

    fun removeUidFromList(uid: Long) {
        val tempList = listOfComponentUidsForAnalysis.value.toMutableList()
        tempList.remove(uid)
        _listOfComponentUidsForAnalysis.value = tempList
    }

    fun runAnalysis() {

    }
}
