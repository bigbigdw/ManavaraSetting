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
        val TESTKEY = stringPreferencesKey("TEST")

        val BESTWORKER_TIME = stringPreferencesKey("BESTWORKER_TIME")
        val BESTWORKER_STATUS = stringPreferencesKey("BESTWORKER_STATUS")

        val JSONWORKER_TIME = stringPreferencesKey("JSONWORKER_TIME")
        val JSONWORKER_STATUS = stringPreferencesKey("JSONWORKER_STATUS")

        val TROPHYWORKER_TIME = stringPreferencesKey("TROPHYWORKER_TIME")
        val TROPHYWORKER_STATUS = stringPreferencesKey("TROPHYWORKER_STATUS")

        val TEST_TIME = stringPreferencesKey("TEST_TIME")
        val TEST_STATUS = stringPreferencesKey("TEST_STATUS")
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
