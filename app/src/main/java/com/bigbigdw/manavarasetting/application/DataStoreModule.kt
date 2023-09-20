package com.bigbigdw.manavarasetting.application

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context : Context) {

    private val Context.dataStore  by preferencesDataStore(name = "dataStore")

    private val stringKey = stringPreferencesKey("textKey")
    private val intCount = intPreferencesKey("count")

    // stringKey 키 값과 대응되는 값 반환
    val text : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[stringKey] ?: ""
        }

    val count : Flow<Int> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[intCount] ?: 0
        }

    // String값을 stringKey 키 값에 저장
    suspend fun setText(text : String){
        context.dataStore.edit { preferences ->
            preferences[stringKey] = text
        }
    }

    suspend fun setCount(count : Int){
        context.dataStore.edit { preferences ->
            preferences[intCount] = count + 1
        }
    }

}