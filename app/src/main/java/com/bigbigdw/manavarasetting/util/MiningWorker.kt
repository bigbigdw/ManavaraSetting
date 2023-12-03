package com.bigbigdw.manavarasetting.util

import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import java.util.concurrent.TimeUnit

object MiningWorker {
    fun doWorkerPeriodic(
        workManager: WorkManager,
        time: Long,
        timeUnit: TimeUnit? = TimeUnit.HOURS,
        tag: String,
        platform: String,
        type: String
    ){

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.WORKER, tag)
            .putString(FirebaseWorkManager.PLATFORM, platform)
            .putString(FirebaseWorkManager.TYPE, type)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(time, timeUnit ?: TimeUnit.HOURS)
            .addTag("${tag}_${platform}_${type}")

            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInputData(inputData)
            .build()


        workManager.enqueueUniquePeriodicWork(
            "${tag}_${platform}_${type}",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }

    fun doWorkerOnetime(
        workManager: WorkManager,
        time: Long,
        timeUnit: TimeUnit = TimeUnit.HOURS,
        tag: String,
        platform: String,
        type: String
    ){

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.WORKER, tag)
            .putString(FirebaseWorkManager.PLATFORM, platform)
            .putString(FirebaseWorkManager.TYPE, type)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(FirebaseWorkManager::class.java)
            .addTag("${tag}_${platform}_${type}")
            .setInputData(inputData)
            .setInitialDelay(time, timeUnit)
            .build()

        workManager.enqueueUniqueWork(
            "${tag}_${platform}_${type}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelWorker(workManager: WorkManager, tag: String, platform : String, type: String){
        workManager.cancelAllWorkByTag("${tag}_${platform}_${type}")
    }

    fun cancelAllWorker(workManager: WorkManager){
        workManager.cancelAllWork()
    }

    fun checkWorker(workManager: WorkManager, tag: String, platform : String, type: String) : String {

        val status = workManager.getWorkInfosByTag("${tag}_${platform}_${type}").get()


//        Log.d("checkWorker", workManager.lastCancelAllTimeMillis.toString())

        if (status.isNullOrEmpty()) {

            Log.d("checkWorker", "${tag}_${platform}_${type}  활성화 되지 않음")
        } else {
            Log.d("checkWorker", "${tag}_${platform}_${type} ${status[0].state.name}")
        }

        return if (status.isNullOrEmpty()) {
            "활성화 되지 않음"
        } else {
            status[0].state.name
        }
    }
}