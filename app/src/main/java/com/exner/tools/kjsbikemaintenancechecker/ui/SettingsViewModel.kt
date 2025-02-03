package com.exner.tools.kjsbikemaintenancechecker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.exner.tools.kjsbikemaintenancechecker.preferences.UserPreferencesManager
import com.exner.tools.kjsbikemaintenancechecker.state.ThemeStateHolder
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager,
    private val themeStateHolder: ThemeStateHolder
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
        themeStateHolder.updateTheme(newTheme)
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