package com.bigbigdw.manavarasetting.main.viewModels

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

        val BESTWORKER_TIME = stringPreferencesKey("BESTWORKER_TIME")
        val JSONWORKER_TIME = stringPreferencesKey("JSONWORKER_TIME")
        val TROPHYWORKER_TIME = stringPreferencesKey("TROPHYWORKER_TIME")
        val TEST_TIME = stringPreferencesKey("TEST_TIME")

        val FCM_TOKEN = stringPreferencesKey("FCM_TOKEN")

        val FCM_COUNT_TEST = stringPreferencesKey("FCM_COUNT_TEST")
        val FCM_COUNT_TEST_TODAY = stringPreferencesKey("FCM_COUNT_TEST_TODAY")
        val FCM_COUNT_BEST = stringPreferencesKey("FCM_COUNT_BEST")
        val FCM_COUNT_BEST_TODAY = stringPreferencesKey("FCM_COUNT_BEST_TODAY")
        val FCM_COUNT_JSON = stringPreferencesKey("FCM_COUNT_JSON")
        val FCM_COUNT_JSON_TODAY = stringPreferencesKey("FCM_COUNT_JSON_TODAY")
        val FCM_COUNT_TROPHY = stringPreferencesKey("FCM_COUNT_TROPHY")
        val FCM_COUNT_TROPHY_TODAY = stringPreferencesKey("FCM_COUNT_TROPHY_TODAY")

        val TIMEMILL_TEST = stringPreferencesKey("TIMEMILL_TEST")
        val TIMEMILL_BEST = stringPreferencesKey("TIMEMILL_BEST")
        val TIMEMILL_JSON = stringPreferencesKey("TIMEMILL_JSON")
        val TIMEMILL_TROPHY = stringPreferencesKey("TIMEMILL_TROPHY")
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
