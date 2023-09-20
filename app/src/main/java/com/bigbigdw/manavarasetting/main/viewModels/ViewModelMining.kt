package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.ManavaraSetting
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import com.bigbigdw.manavarasetting.main.event.EventMining
import com.bigbigdw.manavarasetting.main.event.StateMining
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ViewModelMining @Inject constructor() : ViewModel() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "MINING")

    private val events = Channel<EventMining>()

    val state: StateFlow<StateMining> = events.receiveAsFlow()
        .runningFold(StateMining(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateMining())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateMining, event: EventMining): StateMining {
        return when(event){
            EventMining.Loaded -> {
                current.copy(Loaded = true)
            }
        }
    }

    fun checkWorkerStatus(workManager: WorkManager){
        val status = workManager.getWorkInfosByTag("ManavaraBest").get()

        viewModelScope.launch {
            _sideEffects.send(
                if (status.isEmpty()) {
                    "활성화 되지 않음"
                } else {
                    status[0].state.name
                }
            )
        }
    }

    fun doAutoMining(workManager: WorkManager){
        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, "BEST")
            .build()

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("ManavaraBest")
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "ManavaraBest",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        val status = workManager.getWorkInfosByTag("ManavaraBest").get()

        viewModelScope.launch {
            _sideEffects.send(
                if (status.isEmpty()) {
                    "활성화 되지 않음"
                } else {
                    "크롤링 시작 == ${status[0].state.name}"
                }
            )
        }
    }

    fun cancelAutoMining(workManager: WorkManager){
        workManager.cancelAllWork()

        val status = workManager.getWorkInfosByTag("ManavaraBest").get()

        viewModelScope.launch {
            _sideEffects.send(
                if (status.isEmpty()) {
                    "활성화 되지 않음"
                } else {
                    "크롤링 중지 == ${status[0].state.name}"
                }
            )
        }
    }

    fun incrementCounter() {
        viewModelScope.launch {
            val text = "Sample"
            ManavaraSetting.getInstance().getDataStore().setText(text)
        }
    }
}