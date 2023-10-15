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

        val MINING_NAVER_SERIES_COMIC = stringPreferencesKey("MINING_NAVER_SERIES_COMIC")
        val MINING_NAVER_SERIES_NOVEL = stringPreferencesKey("MINING_NAVER_SERIES_NOVEL")
        val MINING_JOARA_NOVEL = stringPreferencesKey("MINING_JOARA_NOVEL")
        val MINING_JOARA_PREMIUM_NOVEL = stringPreferencesKey("MINING_JOARA_PREMIUM_NOVEL")
        val MINING_JOARA_NOBLESS_NOVEL = stringPreferencesKey("MINING_JOARA_NOBLESS_NOVEL")
        val MINING_NAVER_CHALLENGE_NOVEL = stringPreferencesKey("MINING_NAVER_CHALLENGE_NOVEL")
        val MINING_NAVER_BEST_NOVEL = stringPreferencesKey("MINING_NAVER_BEST_NOVEL")
        val MINING_NAVER_WEBNOVEL_PAY_NOVEL = stringPreferencesKey("MINING_NAVER_WEBNOVEL_PAY_NOVEL")
        val MINING_NAVER_WEBNOVEL_FREE_NOVEL = stringPreferencesKey("MINING_NAVER_WEBNOVEL_FREE_NOVEL")
        val MINING_RIDI_FANTAGY_NOVEL = stringPreferencesKey("MINING_RIDI_FANTAGY_NOVEL")
        val MINING_RIDI_ROMANCE_NOVEL = stringPreferencesKey("MINING_RIDI_ROMANCE_NOVEL")
        val MINING_RIDI_ROMANCE_FANTAGY_NOVEL = stringPreferencesKey("MINING_RIDI_ROMANCE_FANTAGY_NOVEL")
        val MINING_ONESTORY_FANTAGY_NOVEL = stringPreferencesKey("MINING_ONESTORY_FANTAGY_NOVEL")
        val MINING_ONESTORY_ROMANCE_NOVEL = stringPreferencesKey("MINING_ONESTORY_ROMANCE_NOVEL")
        val MINING_ONESTORY_PASS_FANTAGY_NOVEL = stringPreferencesKey("MINING_ONESTORY_PASS_FANTAGY_NOVEL")
        val MINING_ONESTORY_PASS_ROMANCE_NOVEL = stringPreferencesKey("MINING_ONESTORY_PASS_ROMANCE_NOVEL")
        val MINING_KAKAO_STAGE_NOVEL = stringPreferencesKey("MINING_KAKAO_STAGE_NOVEL")
        val MINING_MUNPIA_PAY_NOVEL = stringPreferencesKey("MINING_MUNPIA_PAY_NOVEL")
        val MINING_MUNPIA_FREE_NOVEL = stringPreferencesKey("MINING_MUNPIA_NOVEL")
        val MINING_TOKSODA_NOVEL = stringPreferencesKey("MINING_TOKSODA_NOVEL")
        val MINING_TOKSODA_FREE_NOVEL = stringPreferencesKey("MINING_TOKSODA_FREE_NOVEL")
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
