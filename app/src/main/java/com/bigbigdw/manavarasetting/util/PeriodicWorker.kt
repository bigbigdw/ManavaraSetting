package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.widget.Toast
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doWorker(workManager: WorkManager, repeatInterval: Long, tag: String, timeMill: TimeUnit){
        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.TYPE, tag)
            .build()

        /* 반복 시간에 사용할 수 있는 가장 짧은 최소값은 15 */
        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(repeatInterval, timeMill)
//            .setBackoffCriteria(
//                BackoffPolicy.LINEAR,
//                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
//                TimeUnit.MILLISECONDS
//            )
            .addTag(tag)
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false) // 슬립 모드에서 실행
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

    fun checkWorker(workManager: WorkManager, context : Context, tag : String) : String {

        val status = workManager.getWorkInfosByTag(tag).get()

        val comment = if (status.isEmpty()) {
            "활성화 되지 않음"
        } else {
            "WORKER 상태 == ${status[0].state.name}"
        }

        Toast.makeText(context, comment, Toast.LENGTH_SHORT).show()

        return status[0].state.name
    }
}