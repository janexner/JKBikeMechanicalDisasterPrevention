package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import android.util.Log
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

private const val TAG = "LoadTemplatesFromJsonAndImport"

@OptIn(ExperimentalStdlibApi::class)
fun loadTemplateActivitiesJsonAndImport(
    language: String,
    importDataCallback: (List<TemplateActivity>) -> Unit
): Boolean {
    var result = true

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
                        importDataCallback(newTemplateActivities)
                        Log.d(TAG, "Import done.")
                    }
                }
            } catch (exception: Exception) {
                Log.d(TAG, "Exception $exception")
                result = false
            }
        }
    } else {
        Log.d(TAG, "Attempt to load unknown language $language. Not loading.")
        result = false
    }
    return result
}
