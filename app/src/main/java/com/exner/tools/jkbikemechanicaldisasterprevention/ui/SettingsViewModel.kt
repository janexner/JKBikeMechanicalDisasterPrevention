package com.exner.tools.jkbikemechanicaldisasterprevention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}