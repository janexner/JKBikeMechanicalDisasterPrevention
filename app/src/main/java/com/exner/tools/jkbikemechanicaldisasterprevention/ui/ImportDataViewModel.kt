package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Activity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
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

    fun setFile(file: PlatformFile?) {
        if (file != null) {
            _file.value = file
            _importStateFlow.value = ImportState(ImportStateConstants.FILE_SELECTED)
            analyseFile(file)
        }
    }

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _listOfBikesInFile: MutableStateFlow<List<Bike>> = MutableStateFlow(emptyList())
    val listOfBikesInFile: StateFlow<List<Bike>> = _listOfBikesInFile
    private val _listOfBikesOld: MutableStateFlow<List<Bike>> = MutableStateFlow(emptyList())
    val listOfBikesOld: StateFlow<List<Bike>> = _listOfBikesOld
    private val _listOfBikesNew: MutableStateFlow<List<Bike>> = MutableStateFlow(emptyList())
    val listOfBikesNew: StateFlow<List<Bike>> = _listOfBikesNew
    private val _listOfBikesClashing: MutableStateFlow<List<Bike>> = MutableStateFlow(emptyList())
    val listOfBikesClashing: StateFlow<List<Bike>> = _listOfBikesClashing
    private val _highestUidInBikeDB: MutableStateFlow<Long> = MutableStateFlow(-1)
    private val highestUidInBikeDB: StateFlow<Long> = _highestUidInBikeDB

    private val _listOfActivitiesInFile: MutableStateFlow<List<Activity>> =
        MutableStateFlow(emptyList())
    val listOfActivitiesInFile: StateFlow<List<Activity>> = _listOfActivitiesInFile
    private val _listOfActivitiesOld: MutableStateFlow<List<Activity>> =
        MutableStateFlow(emptyList())
    val listOfActivitiesOld: StateFlow<List<Activity>> = _listOfActivitiesOld
    private val _listOfActivitiesNew: MutableStateFlow<List<Activity>> =
        MutableStateFlow(emptyList())
    val listOfActivitiesNew: StateFlow<List<Activity>> = _listOfActivitiesNew
    private val _listOfActivitiesClashing: MutableStateFlow<List<Activity>> =
        MutableStateFlow(emptyList())
    val listOfActivitiesClashing: StateFlow<List<Activity>> = _listOfActivitiesClashing
    private val _highestUidInActivityDB: MutableStateFlow<Long> = MutableStateFlow(-1)
    private val highestUidInActivityDB: StateFlow<Long> = _highestUidInActivityDB

    private val _listOfTemplateActivitiesInFile: MutableStateFlow<List<TemplateActivity>> =
        MutableStateFlow(emptyList())
    val listOfTemplateActivitiesInFile: StateFlow<List<TemplateActivity>> =
        _listOfTemplateActivitiesInFile
    private val _listOfTemplateActivitiesOld: MutableStateFlow<List<TemplateActivity>> =
        MutableStateFlow(emptyList())
    val listOfTemplateActivitiesOld: StateFlow<List<TemplateActivity>> =
        _listOfTemplateActivitiesOld
    private val _listOfTemplateActivitiesNew: MutableStateFlow<List<TemplateActivity>> =
        MutableStateFlow(emptyList())
    val listOfTemplateActivitiesNew: StateFlow<List<TemplateActivity>> =
        _listOfTemplateActivitiesNew
    private val _listOfTemplateActivitiesClashing: MutableStateFlow<List<TemplateActivity>> =
        MutableStateFlow(emptyList())
    val listOfTemplateActivitiesClashing: StateFlow<List<TemplateActivity>> =
        _listOfTemplateActivitiesClashing
    private val _highestUidInTemplateActivityDB: MutableStateFlow<Long> = MutableStateFlow(-1)
    private val highestUidInTemplateActivityDB: StateFlow<Long> = _highestUidInTemplateActivityDB

    @OptIn(ExperimentalStdlibApi::class)
    private fun analyseFile(file: PlatformFile) {
        viewModelScope.launch {
            val moshi = Moshi.Builder()
                .add(LocalDateJsonAdapter())
                .add(InstantJsonAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter: JsonAdapter<RootData> =
                moshi.adapter<RootData>()
            try {
                val fileContent = file.readBytes().toString(Charsets.UTF_8)
                Log.d("ImportDataVM", "File content: '$fileContent'")
                val newData: RootData? = jsonAdapter.fromJson(fileContent)
                if (newData != null) {
                    val newBikes = newData.bikes
                    // compare with existing
                    _listOfBikesInFile.value = newBikes
                    val oldBikes = repository.getAllBikes()
                    val oldBikeUids: MutableList<Long> = mutableListOf()
                    oldBikes.forEach { oldProcess ->
                        oldBikeUids.add(oldProcess.uid)
                        if (oldProcess.uid > highestUidInBikeDB.value) {
                            _highestUidInBikeDB.value = oldProcess.uid
                        }
                    }
                    _listOfBikesOld.value = emptyList()
                    _listOfBikesClashing.value = emptyList()
                    _listOfBikesNew.value = emptyList()
                    newBikes.forEach { newProcess ->
                        if (oldBikeUids.contains(newProcess.uid)) {
                            // is it the same?
                            if (newProcess == repository.getBikeByUid(newProcess.uid)) {
                                // it is the same. No need to import
                                val temp = listOfBikesOld.value.toMutableList()
                                temp.add(newProcess)
                                _listOfBikesOld.value = temp
                            } else {
                                val temp = listOfBikesClashing.value.toMutableList()
                                temp.add(newProcess)
                                _listOfBikesClashing.value = temp
                            }
                        } else {
                            val temp = listOfBikesNew.value.toMutableList()
                            temp.add(newProcess)
                            _listOfBikesNew.value = temp
                        }
                    }
                    val newActivities = newData.activities
                    // compare with existing
                    _listOfActivitiesInFile.value = newActivities
                    val oldActivities = repository.getAllActivities()
                    val oldActivityUids: MutableList<Long> = mutableListOf()
                    oldActivities.forEach { oldProcess ->
                        oldActivityUids.add(oldProcess.uid)
                        if (oldProcess.uid > highestUidInActivityDB.value) {
                            _highestUidInActivityDB.value = oldProcess.uid
                        }
                    }
                    _listOfActivitiesOld.value = emptyList()
                    _listOfActivitiesClashing.value = emptyList()
                    _listOfActivitiesNew.value = emptyList()
                    newActivities.forEach { newProcess ->
                        if (oldActivityUids.contains(newProcess.uid)) {
                            // is it the same?
                            if (newProcess == repository.getActivityByUid(newProcess.uid)) {
                                // it is the same. No need to import
                                val temp = listOfActivitiesOld.value.toMutableList()
                                temp.add(newProcess)
                                _listOfActivitiesOld.value = temp
                            } else {
                                val temp = listOfActivitiesClashing.value.toMutableList()
                                temp.add(newProcess)
                                _listOfActivitiesClashing.value = temp
                            }
                        } else {
                            val temp = listOfActivitiesNew.value.toMutableList()
                            temp.add(newProcess)
                            _listOfActivitiesNew.value = temp
                        }
                    }
                    val newTemplates = newData.templateActivities
                    // compare with existing
                    _listOfTemplateActivitiesInFile.value = newTemplates
                    val oldTemplateActivities = repository.getAllTemplateActivities()
                    val oldTemplateActivityUids: MutableList<Long> = mutableListOf()
                    oldTemplateActivities.forEach { oldProcess ->
                        oldTemplateActivityUids.add(oldProcess.uid)
                        if (oldProcess.uid > highestUidInActivityDB.value) {
                            _highestUidInActivityDB.value = oldProcess.uid
                        }
                    }
                    _listOfTemplateActivitiesOld.value = emptyList()
                    _listOfTemplateActivitiesClashing.value = emptyList()
                    _listOfTemplateActivitiesNew.value = emptyList()
                    newTemplates.forEach { newProcess ->
                        if (oldTemplateActivityUids.contains(newProcess.uid)) {
                            // is it the same?
                            if (newProcess == repository.getTemplateActivityByUid(newProcess.uid)) {
                                // it is the same. No need to import
                                val temp = listOfTemplateActivitiesOld.value.toMutableList()
                                temp.add(newProcess)
                                _listOfTemplateActivitiesOld.value = temp
                            } else {
                                val temp = listOfTemplateActivitiesClashing.value.toMutableList()
                                temp.add(newProcess)
                                _listOfTemplateActivitiesClashing.value = temp
                            }
                        } else {
                            val temp = listOfTemplateActivitiesNew.value.toMutableList()
                            temp.add(newProcess)
                            _listOfTemplateActivitiesNew.value = temp
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