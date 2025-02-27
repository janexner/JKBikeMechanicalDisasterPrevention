package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.ComponentAnalysisResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class ComponentAnalysisViewModel @Inject constructor(
    val repository: KJsRepository
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

    private val _componentAnalysisResults: MutableStateFlow<ComponentAnalysisResults?> = MutableStateFlow(null)
    val componentAnalysisResults: StateFlow<ComponentAnalysisResults?> = _componentAnalysisResults

    fun runAnalysis() {
        var totalComponents = 0
        var totalMiles = 0
        var totalUsageDays = 0
        viewModelScope.launch {
            listOfComponentUidsForAnalysis.value.forEach { componentUid ->
                totalComponents++
                val component = repository.getComponentByUid(componentUid)
                if (component != null) {
                    totalMiles += component.currentMileage ?: 0
                    if (component.retirementDate != null && component.firstUseDate != null) {
                        val lifetimeInDays = component.firstUseDate.daysUntil(component.retirementDate)
                        totalUsageDays += lifetimeInDays
                    }
                }
            }
            val result = ComponentAnalysisResults(
                totalUsageMiles = totalMiles,
                totalUsageDays = totalUsageDays,
            )
            _componentAnalysisResults.value = result
        }
    }
}
