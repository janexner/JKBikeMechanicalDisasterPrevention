package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.tools.loadTemplateActivitiesJsonAndImport
import com.exner.tools.jkbikemechanicaldisasterprevention.preferences.UserPreferencesManager
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    val repository: KJsRepository
) : ViewModel() {

    val userSelectedTheme: StateFlow<Theme> = userPreferencesManager.theme().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        Theme.Auto
    )

    fun updateUserSelectedTheme(newTheme: Theme) {
        viewModelScope.launch {
            userPreferencesManager.setTheme(newTheme)
        }
    }

    val todoListsExpire: StateFlow<Boolean> = userPreferencesManager.todoListsExpire().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun updateTodoListsExpire(newExpire: Boolean) {
        viewModelScope.launch {
            userPreferencesManager.setTodoListsExpire(newExpire)
        }
    }

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