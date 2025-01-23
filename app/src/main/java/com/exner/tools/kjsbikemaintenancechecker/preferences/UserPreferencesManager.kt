package com.exner.tools.kjsbikemaintenancechecker.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.exner.tools.kjsbikemaintenancechecker.ui.theme.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val userDataStorePreferences = appContext.dataStore

    fun theme(): Flow<Theme> {
        return userDataStorePreferences.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            Theme.valueOf(preferences[KEY_THEME] ?: Theme.Auto.name)
        }
    }

    suspend fun setTheme(newTheme: Theme) {
        userDataStorePreferences.edit { preferences ->
            preferences[KEY_THEME] = newTheme.name
        }
    }

    fun defaultBikeUidShort(): Flow<Long> {
        return userDataStorePreferences.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[KEY_DEFAULT_BIKE_UID_SHORT] ?: -1L
        }
    }

    suspend fun setDefaultBikeUidShort(newUid: Long) {
        userDataStorePreferences.edit { preferences ->
            preferences[KEY_DEFAULT_BIKE_UID_SHORT] = newUid
        }
    }

    private companion object {
        val KEY_THEME = stringPreferencesKey(name = "preference_theme")
        val KEY_DEFAULT_BIKE_UID_SHORT = longPreferencesKey(name = "preference_default_bike_uid_short")
    }
}