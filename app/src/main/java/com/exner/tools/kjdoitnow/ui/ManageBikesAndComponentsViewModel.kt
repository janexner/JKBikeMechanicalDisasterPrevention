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
