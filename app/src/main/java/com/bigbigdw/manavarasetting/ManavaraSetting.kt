package com.bigbigdw.manavarasetting

import android.app.Application
import com.bigbigdw.manavarasetting.application.DataStoreModule

class ManavaraSetting : Application() {

    private lateinit var dataStore : DataStoreModule
    companion object {
        private lateinit var sampleApplication: ManavaraSetting
        fun getInstance() : ManavaraSetting = sampleApplication
    }
    override fun onCreate() {
        super.onCreate()
        sampleApplication = this
        dataStore = DataStoreModule(this)
    }

    fun getDataStore() : DataStoreModule = dataStore
}