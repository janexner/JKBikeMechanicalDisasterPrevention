package com.exner.tools.jkbikemechanicaldisasterprevention.ui

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

private const val TAG = "ManageTemplateActivitiesVM"

@HiltViewModel
class ManageTemplateActivitiesViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val templateActivities = repository.observeTemplateActivity

    @OptIn(ExperimentalStdlibApi::class)
    fun replaceTemplateActivitiesWithNewLanguage(language: String) {
        // check whether that language is OK
        if (language in arrayOf("en", "fr", "de", "de-rCH")) {
            val moshi = Moshi.Builder()
                .add(LocalDateJsonAdapter())
                .add(InstantJsonAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter: JsonAdapter<RootData> =
                moshi.adapter<RootData>()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // download JSON
                    val githubReleaseURL =
                        URL("https://api.github.com/repos/janexner/JKBikeTemplateActivities/releases/latest")
                    val githubReleaseString = githubReleaseURL.readText(Charsets.UTF_8)
                    Log.d(TAG, "github release: $githubReleaseString")
                    val releaseStringParts = githubReleaseString.split(",")
                    var tagName = ""
                    releaseStringParts.forEach { stringPart ->
                        val tupel = stringPart.split(":")
                        if (tupel[0] == "\"tag_name\"") {
                            tagName = tupel[1].replace("\"", "")
                        }
                    }
                    Log.d(TAG, "Release tag is $tagName")
                    val templateURL =
                        URL("https://raw.githubusercontent.com/janexner/JKBikeTemplateActivities/refs/tags/$tagName/$language/template-activities.json")
                    val templateContent = templateURL.readText(Charsets.UTF_8)
                    val newRootData = jsonAdapter.fromJson(templateContent)
                    if (newRootData != null) {
                        val newTemplateActivities = newRootData.templateActivities
                        if (newTemplateActivities != null) {
                            viewModelScope.launch {
                                // clear out templates
                                repository.deleteAllTemplateActivities()
                                // import JSON
                                newTemplateActivities.forEach { templateActivity ->
                                    repository.insertTemplateActivity(templateActivity)
                                }
                                Log.d(TAG, "Import done.")
                            }
                        }
                    }
                } catch (exception: Exception) {
                    Log.d(TAG, "Exception $exception")
                }
            }
        } else {
            Log.d(TAG, "Attempt to load unknown language $language. Not loading.")
        }
    }
}
