package com.bigbigdw.manavarasetting.util

import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doWorker(
        workManager: WorkManager,
        delayMills: Long,
        tag: String,
        platform: String,
        type: String
    ){

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.WORKER, tag)
            .putString(FirebaseWorkManager.PLATFORM, platform)
            .putString(FirebaseWorkManager.TYPE, type)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(4, TimeUnit.HOURS)
            .addTag("${tag}_${platform}_${type}")

            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInputData(inputData)
            .build()

        val currentUser :  FirebaseUser?
        val auth: FirebaseAuth = Firebase.auth

        currentUser = auth.currentUser

//        if(currentUser?.uid == "A8uh2QkVQaV3Q3rE8SgBNKzV6VH2"){
//            workManager.enqueueUniquePeriodicWork(
//                "${tag}_${platform}_${type}",
//                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
//                workRequest
//            )
//        }

        workManager.enqueueUniquePeriodicWork(
            "${tag}_${platform}_${type}",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
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