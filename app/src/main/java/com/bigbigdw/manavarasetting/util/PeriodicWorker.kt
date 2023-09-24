package com.bigbigdw.manavarasetting.util

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doWorker(workManager: WorkManager, repeatInterval: Long, tag: String, timeMill: TimeUnit){
        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, tag)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(repeatInterval, timeMill)
            .addTag(tag)
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }

    fun cancelWorker(workManager: WorkManager, tag : String){
        workManager.cancelAllWorkByTag(tag)
    }

    fun cancelAllWorker(workManager: WorkManager){
        workManager.cancelAllWork()
    }

    fun checkWorker(workManager: WorkManager, tag: String) : String {

        val status = workManager.getWorkInfosByTag(tag).get()

        return if (status.isNullOrEmpty()) {
            "활성화 되지 않음"
        } else {
            status[0].state.name
        }
    }
}