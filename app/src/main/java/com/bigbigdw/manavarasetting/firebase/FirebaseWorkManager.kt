package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.setDataStore
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    val dataStore = DataStoreManager(context)

    companion object {
        const val WORKER = "WORKER"
        const val PLATFORM = "PLATFORM"
        const val TYPE = "TYPE"
    }

    override fun doWork(): Result {

        val year = DBDate.dateMMDDHHMM().substring(0, 4)
        val month = DBDate.dateMMDDHHMM().substring(4, 6)
        val day = DBDate.dateMMDDHHMM().substring(6, 8)
        val hour = DBDate.dateMMDDHHMM().substring(8, 10)
        val min = DBDate.dateMMDDHHMM().substring(10, 12)

        val workerName =
            inputData.getString(WORKER) + "_" + inputData.getString(PLATFORM) + "_" + inputData.getString(
                TYPE
            )

        if (inputData.getString(PLATFORM) == "NAVER_SERIES") {
            if (inputData.getString(TYPE) == "COMIC") {
                runBlocking {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.mining(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                        context = applicationContext
                    )
                }
            } else {
                runBlocking {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.mining(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                        context = applicationContext
                    )
                }
            }
        } else if (inputData.getString(PLATFORM)?.contains("JOARA") == true) {
            runBlocking {
                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_MONTH").removeValue()
                }

                if (DBDate.datedd() == "01") {
                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_MONTH").removeValue()
                }

                MiningSource.mining(
                    platform = inputData.getString(PLATFORM) ?: "",
                    type = inputData.getString(TYPE) ?: "",
                    context = applicationContext
                )
            }
        } else if (inputData.getString(PLATFORM)?.contains("NAVER_CHALLENGE") == true) {
            runBlocking {
                runBlocking {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: ""
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.mining(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                        context = applicationContext
                    )
                }
            }
        }


        postFCM(
            data = workerName,
            time = "${year}.${month}.${day} ${hour}:${min}",
            activity = "${inputData.getString(PLATFORM)} ${inputData.getString(TYPE)}",
        )

        setDataStore(data = workerName)

        return Result.success()
    }

    private fun postFCM(data: String, time: String, activity: String = "") {

        val fcmBody = DataFCMBody(
            "/topics/adminAll",
            "high",
            DataFCMBodyData("마나바라 세팅", data),
            DataFCMBodyNotification(
                title = "마나바라 세팅",
                body = "$time $data",
                click_action = "best"
            ),
        )

        miningAlert("마나바라 세팅", "$time $data", "ALERT", activity = activity)



        val call = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FirebaseService::class.java)
            .postRetrofit(
                fcmBody
            )

        call.enqueue(object : Callback<FWorkManagerResult?> {
            override fun onResponse(
                call: Call<FWorkManagerResult?>,
                response: retrofit2.Response<FWorkManagerResult?>
            ) {
                if (response.isSuccessful) {
                    Log.d("FCM", "성공")
                } else {
                    Log.d("FCM", "실패2")
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                Log.d("FCM", "실패");
            }
        })
    }

    private fun miningAlert(
        title: String,
        message: String,
        child: String,
        activity: String = ""
    ) {
        FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)
            .child(DBDate.dateMMDDHHMM()).setValue(
                FCMAlert(
                    date = DBDate.dateMMDDHHMM(),
                    title = title,
                    body = message,
                    activity = activity
                )
            )
    }
}