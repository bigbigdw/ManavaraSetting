package com.bigbigdw.manavarasetting.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("MANAVARASETTING")

        val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")

        val BEST_NAVER_SERIES_COMIC = stringPreferencesKey("BEST_NAVER_SERIES_COMIC")
        val BEST_NAVER_SERIES_NOVEL = stringPreferencesKey("BEST_NAVER_SERIES_NOVEL")

        val JSON_NAVER_SERIES_COMIC = stringPreferencesKey("JSON_NAVER_SERIES_COMIC")
        val JSON_NAVER_SERIES_NOVEL = stringPreferencesKey("JSON_NAVER_SERIES_NOVEL")

        val TROPHY_NAVER_SERIES_COMIC = stringPreferencesKey("TROPHY_NAVER_SERIES_COMIC")
        val TROPHY_NAVER_SERIES_NOVEL = stringPreferencesKey("TROPHY_NAVER_SERIES_NOVEL")

    }

    fun getDataStoreString(key : Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }
    }

    suspend fun setDataStoreString(key : Preferences.Key<String>, str: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = str
        }
    }

}
