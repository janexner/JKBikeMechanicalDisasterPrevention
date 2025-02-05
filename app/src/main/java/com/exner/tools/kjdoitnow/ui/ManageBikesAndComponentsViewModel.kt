package com.exner.tools.kjdoitnow.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.kjdoitnow.database.KJsRepository
import com.exner.tools.kjdoitnow.ui.components.BikeOrComponent
import com.exner.tools.kjdoitnow.ui.components.bikeAndComponentTreeToListOfString
import com.exner.tools.kjdoitnow.ui.components.createBikeAndComponentTree
import com.exner.tools.kjdoitnow.ui.components.flattenWithIndent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageBikesAndComponentsViewModel @Inject constructor(
    repository: KJsRepository
) : ViewModel() {

    private val _flattenedBikesAndComponents: MutableStateFlow<List<BikeOrComponent>> =
        MutableStateFlow(
            emptyList()
        )
    val flattenedBikesAndComponents: StateFlow<List<BikeOrComponent>> = _flattenedBikesAndComponents

    private val collapsedIds: MutableSet<String> = mutableSetOf()
    private val _cidList: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    val cidList: StateFlow<Set<String>> = _cidList

    fun addIdToCollapsedIds(collapseId: String) {
        collapsedIds.add(collapseId)
        _cidList.value = collapsedIds.toMutableSet()
    }
    fun removeIdFromCollapsedIds(collapseId: String) {
        collapsedIds.remove(collapseId)
        _cidList.value = collapsedIds.toMutableSet()
    }

    fun isThisIdCollapsed(collapseId: String, someSet: Set<String>): Boolean {
        Log.d("MBCVM", "${someSet.size}")
        return collapsedIds.contains(collapseId)
    }
    fun isThisIdHidden(collapseIdTags: List<String>, someSet: Set<String>): Boolean {
        Log.d("MBCVM", "${someSet.size}")
        collapsedIds.forEach { id ->
            if (collapseIdTags.contains(id)) {
                return true
            }
        }
        return false
    }

    init {
        viewModelScope.launch {
            val bikeAndComponentTree = createBikeAndComponentTree(repository)
            val treeAsStrings = bikeAndComponentTree.bikeAndComponentTreeToListOfString()
            if (treeAsStrings.isNotEmpty()) {
                treeAsStrings.forEach { line ->
                    Log.d("TEST", line)
                }
            }
            val treeFlattened = bikeAndComponentTree.flattenWithIndent()
            _flattenedBikesAndComponents.value = treeFlattened
        }
    }
}
