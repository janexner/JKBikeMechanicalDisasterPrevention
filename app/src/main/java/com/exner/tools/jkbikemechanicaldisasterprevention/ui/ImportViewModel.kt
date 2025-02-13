package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.InstantJsonAdapter
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.LocalDateJsonAdapter
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.RootData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ImportStateConstants {
    IDLE,
    FILE_SELECTED,
    FILE_ANALYSED,
    IMPORT_FINISHED,
    ERROR
}

data class ImportState(
    val state: ImportStateConstants = ImportStateConstants.IDLE
)

@HiltViewModel
class ImportDataViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    private val _importStateFlow = MutableStateFlow(ImportState())
    val importStateFlow: StateFlow<ImportState> = _importStateFlow

    private var _file: MutableStateFlow<PlatformFile?> = MutableStateFlow(null)
    val file: StateFlow<PlatformFile?> = _file

    private val _override: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val override: StateFlow<Boolean> = _override

    private val _listOfBikesInFile: MutableStateFlow<List<Bike>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfBikesInFile: StateFlow<List<Bike>> = _listOfBikesInFile

    private val _listOfOldBikes: MutableStateFlow<List<Bike>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfOldBikes: StateFlow<List<Bike>> = _listOfOldBikes
    private val _listOfNewBikes: MutableStateFlow<List<Bike>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfNewBikes: StateFlow<List<Bike>> = _listOfNewBikes
    private val _listOfClashingBikes: MutableStateFlow<List<Bike>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfClashingBikes: StateFlow<List<Bike>> = _listOfClashingBikes

    private val _listOfComponentsInFile: MutableStateFlow<List<Component>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfComponentsInFile: StateFlow<List<Component>> =
        _listOfComponentsInFile

    private val _listOfOldComponents: MutableStateFlow<List<Component>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfOldComponents: StateFlow<List<Component>> = _listOfOldComponents
    private val _listOfNewComponents: MutableStateFlow<List<Component>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfNewComponents: StateFlow<List<Component>> = _listOfNewComponents
    private val _listOfClashingComponents: MutableStateFlow<List<Component>> =
        MutableStateFlow(
            emptyList()
        )
    val listOfClashingComponents: StateFlow<List<Component>> =
        _listOfClashingComponents

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _highestUidInBikeDB: MutableStateFlow<Long> = MutableStateFlow(-1)
    val highestUidInBikeDB: StateFlow<Long> = _highestUidInBikeDB
    private val _highestUidInComponentDB: MutableStateFlow<Long> = MutableStateFlow(-1)
    val highestUidInComponentDB: StateFlow<Long> = _highestUidInComponentDB

    fun setOverride(override: Boolean) {
        _override.value = override
    }

    fun commitImport(
        successCallback: () -> Unit
    ) {
        if (listOfNewBikes.value.isNotEmpty() && listOfNewComponents.value.isNotEmpty()) {
            viewModelScope.launch {
                if (override.value) {
                    repository.deleteAllComponents()
                    if (listOfComponentsInFile.value.isNotEmpty()) {
                        listOfComponentsInFile.value.forEach { category ->
                            repository.insertComponent(category)
                        }
                    }
                    repository.deleteAllBikes()
                    if (listOfBikesInFile.value.isNotEmpty()) {
                        listOfBikesInFile.value.forEach { process ->
                            repository.insertBike(process)
                        }
                    }
                } else {
                    listOfNewComponents.value.forEach { category ->
                        repository.insertComponent(category)
                    }
                    listOfNewBikes.value.forEach { process ->
                        repository.insertBike(process)
                    }
                }
                _importStateFlow.value = ImportState(ImportStateConstants.IMPORT_FINISHED)
                successCallback()
            }
        }
    }

    fun setFile(file: PlatformFile?) {
        if (file != null) {
            _file.value = file
            _importStateFlow.value = ImportState(ImportStateConstants.FILE_SELECTED)
            analyseFile(file)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun analyseFile(file: PlatformFile) {
        viewModelScope.launch {
            try {
                val fileContent = file.readBytes().toString(Charsets.UTF_8)
                Log.d("ImportDataVM", "File content: '$fileContent'")
                val moshi = Moshi.Builder()
                    .add(LocalDateJsonAdapter())
                    .add((InstantJsonAdapter()))
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                val jsonAdapter: JsonAdapter<RootData> = moshi.adapter<RootData>()
                val data: RootData? = jsonAdapter.fromJson(fileContent)
                if (data != null) {
                    // processes
                    if (data.bikes != null) {
                        val newBikes: List<Bike> = data.bikes
                        // compare with existing
                        _listOfBikesInFile.value = newBikes
                        val oldBikes = repository.getAllBikes()
                        val oldUids: MutableList<Long> = mutableListOf()
                        oldBikes.forEach { oldBike ->
                            oldUids.add(oldBike.uid)
                            if (oldBike.uid > highestUidInBikeDB.value) {
                                _highestUidInBikeDB.value = oldBike.uid
                            }
                        }
                        _listOfOldBikes.value = emptyList()
                        _listOfClashingBikes.value = emptyList()
                        _listOfNewBikes.value = emptyList()
                        newBikes.forEach { newBike ->
                            if (oldUids.contains(newBike.uid)) {
                                // is it the same?
                                if (newBike == repository.getBikeByUid(newBike.uid)) {
                                    // it is the same. No need to import
                                    val temp = listOfOldBikes.value.toMutableList()
                                    temp.add(newBike)
                                    _listOfOldBikes.value = temp
                                } else {
                                    val temp = listOfClashingBikes.value.toMutableList()
                                    temp.add(newBike)
                                    _listOfClashingBikes.value = temp
                                }
                            } else {
                                val temp = listOfNewBikes.value.toMutableList()
                                temp.add(newBike)
                                _listOfNewBikes.value = temp
                            }
                        }
                    }
                    // categories
                    if (data.components != null) {
                        val newComponents: List<Component> = data.components
                        // compare with existing
                        _listOfComponentsInFile.value = newComponents
                        val oldComponents = repository.getAllComponents()
                        val oldComponentUids: MutableList<Long> = mutableListOf()
                        oldComponents.forEach { oldCategory ->
                            oldComponentUids.add(oldCategory.uid)
                            if (oldCategory.uid > highestUidInComponentDB.value) {
                                _highestUidInComponentDB.value = oldCategory.uid
                            }
                        }
                        _listOfOldComponents.value = emptyList()
                        _listOfClashingComponents.value = emptyList()
                        _listOfNewComponents.value = emptyList()
                        newComponents.forEach { newComponent ->
                            if (oldComponentUids.contains(newComponent.uid)) {
                                // is it the same?
                                if (newComponent == repository.getComponentByUid(newComponent.uid)) {
                                    // it is the same. No need to import
                                    val temp = listOfOldComponents.value.toMutableList()
                                    temp.add(newComponent)
                                    _listOfOldComponents.value = temp
                                } else {
                                    val temp = listOfClashingComponents.value.toMutableList()
                                    temp.add(newComponent)
                                    _listOfClashingComponents.value = temp
                                }
                            } else {
                                val temp = listOfNewComponents.value.toMutableList()
                                temp.add(newComponent)
                                _listOfNewComponents.value = temp
                            }
                        }
                    }
                }
                // done
                _importStateFlow.value = ImportState(ImportStateConstants.FILE_ANALYSED)
            } catch (exception: Exception) {
                Log.d("ImportDataVM", "Exception: ${exception.message}")
                _errorMessage.value = exception.message.toString()
                _importStateFlow.value = ImportState(ImportStateConstants.ERROR)
            }
        }
    }
}
