package com.bigbigdw.manavarasetting.util

import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doAutoTest(workManager: WorkManager){
        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, "TEST")
            .build()

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(30, TimeUnit.MINUTES)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag("TEST")
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "TEST",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }

    fun cancelAutoMiningTEST(workManager: WorkManager){
        workManager.cancelAllWork()
    }
}