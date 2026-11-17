package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.ThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mizan_maroc_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val THEME = stringPreferencesKey("theme_preference")
        val CHECKED_DOCS = stringSetPreferencesKey("checked_document_ids")
    }

    private val deviceDefaultLanguage: AppLanguage
        get() = if (context.resources.configuration.locales[0].language.equals("fr", ignoreCase = true)) {
            AppLanguage.FR
        } else {
            AppLanguage.AR
        }

    val languageFlow: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
        when (prefs[Keys.LANGUAGE]) {
            AppLanguage.AR.name -> AppLanguage.AR
            AppLanguage.FR.name -> AppLanguage.FR
            else -> deviceDefaultLanguage
        }
    }

    val themeFlow: Flow<ThemePreference> = context.dataStore.data.map { prefs ->
        when (prefs[Keys.THEME]) {
            ThemePreference.LIGHT.name -> ThemePreference.LIGHT
            ThemePreference.DARK.name -> ThemePreference.DARK
            else -> ThemePreference.SYSTEM
        }
    }

    val checkedDocumentsFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.CHECKED_DOCS] ?: emptySet()
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = language.name
        }
    }

    suspend fun setThemePreference(theme: ThemePreference) {
        context.dataStore.edit { prefs ->
            prefs[Keys.THEME] = theme.name
        }
    }

    suspend fun toggleDocumentChecked(documentId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.CHECKED_DOCS]?.toMutableSet() ?: mutableSetOf()
            if (current.contains(documentId)) {
                current.remove(documentId)
            } else {
                current.add(documentId)
            }
            prefs[Keys.CHECKED_DOCS] = current
        }
    }

    suspend fun clearAllChecklists() {
        context.dataStore.edit { prefs ->
            prefs[Keys.CHECKED_DOCS] = emptySet()
        }
    }
}
