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

        val STATUS_NAVER_SERIES_COMIC_ACTION = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_ACTION")
        val STATUS_NAVER_SERIES_COMIC_ALL = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_ALL")
        val STATUS_NAVER_SERIES_COMIC_BL = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_ALL")
        val STATUS_NAVER_SERIES_COMIC_DRAMA = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_DRAMA")
        val STATUS_NAVER_SERIES_COMIC_MELO = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_MELO")
        val STATUS_NAVER_SERIES_COMIC_YOUNG = stringPreferencesKey("STATUS_NAVER_SERIES_COMIC_YOUNG")

        val STATUS_NAVER_SERIES_NOVEL_ALL = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_ALL")
        val STATUS_NAVER_SERIES_NOVEL_FANTASY = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_FANTASY")
        val STATUS_NAVER_SERIES_NOVEL_MARTIAL_ARTS = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_MARTIAL_ARTS")
        val STATUS_NAVER_SERIES_NOVEL_MODERN_FANTASY = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_MODERN_FANTASY")
        val STATUS_NAVER_SERIES_NOVEL_ROMANCE = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_ROMANCE")
        val STATUS_NAVER_SERIES_NOVEL_ROMANCE_FANTASY = stringPreferencesKey("STATUS_NAVER_SERIES_NOVEL_ROMANCE_FANTASY")

        val STATUS_JOARA_NOVEL_ALL = stringPreferencesKey("STATUS_JOARA_NOVEL_ALL")
        val STATUS_JOARA_NOVEL_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOVEL_FANTAGY")
        val STATUS_JOARA_NOVEL_MARTIAL_ARTS = stringPreferencesKey("STATUS_JOARA_NOVEL_MARTIAL_ARTS")
        val STATUS_JOARA_NOVEL_MODREN_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOVEL_MODREN_FANTAGY")
        val STATUS_JOARA_NOVEL_ROMANCE = stringPreferencesKey("STATUS_JOARA_NOVEL_ROMANCE")
        val STATUS_JOARA_NOVEL_ROMANCE_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOVEL_ROMANCE_FANTAGY")

        val STATUS_JOARA_PREMIUM_NOVEL_ALL = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_ALL")
        val STATUS_JOARA_PREMIUM_NOVEL_FANTAGY = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_FANTAGY")
        val STATUS_JOARA_PREMIUM_NOVEL_MARTIAL_ARTS = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_MARTIAL_ARTS")
        val STATUS_JOARA_PREMIUM_NOVEL_MODREN_FANTAGY = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_MODREN_FANTAGY")
        val STATUS_JOARA_PREMIUM_NOVEL_ROMANCE = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_ROMANCE")
        val STATUS_JOARA_PREMIUM_NOVEL_ROMANCE_FANTAGY = stringPreferencesKey("STATUS_JOARA_PREMIUM_NOVEL_ROMANCE_FANTAGY")

        val STATUS_JOARA_NOBLESS_NOVEL_ALL = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_ALL")
        val STATUS_JOARA_NOBLESS_NOVEL_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_FANTAGY")
        val STATUS_JOARA_NOBLESS_NOVEL_MARTIAL_ARTS = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_MARTIAL_ARTS")
        val STATUS_JOARA_NOBLESS_NOVEL_MODREN_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_MODREN_FANTAGY")
        val STATUS_JOARA_NOBLESS_NOVEL_ROMANCE = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_ROMANCE")
        val STATUS_JOARA_NOBLESS_NOVEL_ROMANCE_FANTAGY = stringPreferencesKey("STATUS_JOARA_NOBLESS_NOVEL_ROMANCE_FANTAGY")
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
