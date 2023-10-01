package com.bigbigdw.manavarasetting.util

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.firebase.FirebaseWorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

object PeriodicWorker {
    fun doWorker(workManager: WorkManager, repeatInterval: Long, tag: String, timeMill: TimeUnit, platform : String, type: String){

        val inputData = Data.Builder()
            .putString(FirebaseWorkManager.WORKER, tag)
            .putString(FirebaseWorkManager.PLATFORM, platform)
            .putString(FirebaseWorkManager.TYPE, type)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FirebaseWorkManager>(repeatInterval, timeMill)
            .addTag("${tag}_${platform}_${type}")
            .setInputData(inputData)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .build()
            )
            .build()

        var currentUser :  FirebaseUser? = null
        val auth: FirebaseAuth = Firebase.auth

        currentUser = auth.currentUser

        if(currentUser?.uid == "A8uh2QkVQaV3Q3rE8SgBNKzV6VH2"){
            workManager.enqueueUniquePeriodicWork(
                tag,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )

            val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

            val time = repeatInterval.toString()

            val unit = if(timeMill == TimeUnit.HOURS){
                "시간"
            } else {
                "분"
            }

            mRootRef.child("TIMEMILL_${tag}").setValue("${time}${unit} 마다")
        }
    }

    fun cancelWorker(workManager: WorkManager, tag: String, platform : String, type: String){
        workManager.cancelAllWorkByTag("${tag}_${platform}_${type}")
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