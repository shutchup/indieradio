package com.indieradio.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.indieradio.ui.theme.skin.SkinTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.skinDataStore: DataStore<Preferences> by preferencesDataStore(name = "skin_preferences")

/**
 * Repository for managing skin theme preferences
 */
@Singleton
class SkinRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.skinDataStore

    companion object {
        private val SKIN_ID_KEY = stringPreferencesKey("selected_skin_id")
    }

    /**
     * Get current skin theme as Flow
     */
    val currentSkin: Flow<SkinTheme> = dataStore.data
        .map { preferences ->
            val skinId = preferences[SKIN_ID_KEY] ?: SkinTheme.ModernMinimal.id
            SkinTheme.fromId(skinId)
        }

    /**
     * Save skin theme preference
     */
    suspend fun setSkin(skinTheme: SkinTheme) {
        dataStore.edit { preferences ->
            preferences[SKIN_ID_KEY] = skinTheme.id
        }
    }
}
