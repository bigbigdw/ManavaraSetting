package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.widget.Toast
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doWorker(workManager: WorkManager, context : Context, repeatInterval : Long, tag : String, timeMill : TimeUnit){
        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, tag)
            .build()

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(repeatInterval, timeMill)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(tag)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        val status = workManager.getWorkInfosByTag(tag).get()

        val comment = if (status.isEmpty()) {
            "활성화 되지 않음"
        } else {
            "WORKER 활성화 == ${status[0].state.name}"
        }

        Toast.makeText(context, comment, Toast.LENGTH_SHORT).show()

    }

    fun cancelWorker(workManager: WorkManager, context : Context, tag : String){
        workManager.cancelAllWork()

        val status = workManager.getWorkInfosByTag(tag).get()

        val comment = if (status.isEmpty()) {
            "활성화 되지 않음"
        } else {
            "WORKER 중지 == ${status[0].state.name}"
        }

        Toast.makeText(context, comment, Toast.LENGTH_SHORT).show()
    }

    fun checkWorker(workManager: WorkManager, context : Context, tag : String){

        val status = workManager.getWorkInfosByTag(tag).get()

        val comment = if (status.isEmpty()) {
            "활성화 되지 않음"
        } else {
            "WORKER 상태 == ${status[0].state.name}"
        }

        Toast.makeText(context, comment, Toast.LENGTH_SHORT).show()
    }
}