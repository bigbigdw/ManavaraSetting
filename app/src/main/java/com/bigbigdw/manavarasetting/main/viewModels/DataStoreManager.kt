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
    }

    // to get the email
    val getTest: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TESTKEY] ?: ""
        }

    // to save the email
    suspend fun setTest(str: String) {
        context.dataStore.edit { preferences ->
            preferences[TESTKEY] = str
        }
    }

}
