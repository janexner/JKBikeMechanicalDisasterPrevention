package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.InstantJsonAdapter
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.LocalDateJsonAdapter
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.RootData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class ExportDataViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val allBikes = repository.observeBikes
    val allActivities = repository.observeActivities
    val allTemplates = repository.observeTemplateActivity

    @OptIn(ExperimentalStdlibApi::class)
    fun commitExport(
        context: Context,
        successCallback: (() -> Unit)? = null,
        failureCallback: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                val moshi = Moshi.Builder()
                    .add(LocalDateJsonAdapter())
                    .add(InstantJsonAdapter())
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                // load data
                val data = bundleData()
                val jsonAdapter: JsonAdapter<RootData> = moshi.adapter<RootData>()
                val overallJsonString = jsonAdapter.toJson(data)
                Log.d("ExportDateVM", "Collected data: $overallJsonString")
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val timeComponentForExportFileName = now.toString()
                // now write it to the Downloads folder
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        "jkbike-export-$timeComponentForExportFileName"
                    )
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/json")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val resolver = context.contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    resolver.openOutputStream(uri).use { stream ->
                        stream?.write(overallJsonString.encodeToByteArray())
                    }
                }
                Log.d("ExportDataVM", "Exported successfully")
                // notify
                if (successCallback != null) {
                    successCallback()
                }
            } catch (exception: Exception) {
                Log.d("ExportData", "Something untoward happened: ${exception.message}")
                if (failureCallback != null) {
                    failureCallback(exception.message ?: "Failure (unknown)")
                }
            }
        }
    }

    private suspend fun bundleData(): RootData {
        val listOfAllBikes = repository.getAllBikes()
        val listOfAllActivities = repository.getAllActivities()
        val listOfTemplateActivities = repository.getAllTemplateActivities()
        val data = RootData(listOfAllBikes, listOfAllActivities, listOfTemplateActivities)
        return data
    }
}