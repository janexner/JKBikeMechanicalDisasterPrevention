package com.exner.tools.jkbikemechanicaldisasterprevention.database

import android.util.Log
import androidx.core.os.LocaleListCompat
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.TemplateActivity
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.loadTemplateActivitiesJsonAndImport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

private const val TAG = "KJsDBCallback"

class KJsDatabaseCallback(
    private val provider: Provider<KJsDAO>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabaseWithSampleData(provider = provider)
            val localeToLoad = determineLocaleToLoad()
            Log.d(TAG, "Going to load $localeToLoad...")
            var newTemplateActivities = emptyList<TemplateActivity>()
            val importWorked = loadTemplateActivitiesJsonAndImport(localeToLoad) { newTAs ->
                newTemplateActivities = newTAs
            }
            if (importWorked && newTemplateActivities.isNotEmpty()) {
                // clear out old templates
                provider.get().deleteAllTemplateActivities()
                // import new list
                newTemplateActivities.forEach { templateActivity ->
                    provider.get().insertTemplateActivity(templateActivity)
                }
            } else {
                generatePreparationTemplateActivities(provider = provider)
            }
        }
    }

    private fun determineLocaleToLoad(): String {
        val listOfDefaultLocales = LocaleListCompat.getDefault()
        Log.d(TAG, "LocaleListCompat: $listOfDefaultLocales")
        val listOfMyLocales = listOf("en", "de", "fr")
        val numLocales = listOfDefaultLocales.size()
        for (i in 0..numLocales) {
            val tempLocale = listOfDefaultLocales.get(i)
            Log.d(TAG, "Step $i: ${tempLocale?.language}/${tempLocale?.country}")
            val tempLanguage = tempLocale?.language
            if (tempLanguage in listOfMyLocales) {
                if (tempLanguage == "de" && tempLocale.country == "CH") {
                    return "de-rCH"
                } else if (tempLanguage != null) {
                    return tempLanguage
                }
            }
        }
        return "en" // fallback
    }
}
