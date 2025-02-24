package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.loadTemplateActivitiesJsonAndImport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ManageTemplateActivitiesVM"

@HiltViewModel
class ManageTemplateActivitiesViewModel @Inject constructor(
    val repository: KJsRepository
) : ViewModel() {

    val templateActivities = repository.observeTemplateActivity

    fun replaceTemplateActivitiesWithNewLanguage(language: String) {
        // check whether that language is OK
        loadTemplateActivitiesJsonAndImport(language) { newTemplateActivities ->
            viewModelScope.launch {
                // clear out templates
                repository.deleteAllTemplateActivities()
                // import List
                newTemplateActivities.forEach { templateActivity ->
                    repository.insertTemplateActivity(templateActivity)
                }
            }
        }
    }
}
