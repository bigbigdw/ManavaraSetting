package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.MiningWorker
import com.bigbigdw.manavarasetting.util.getNextNovelInListEng
import com.bigbigdw.manavarasetting.util.makeTodayJson
import com.bigbigdw.manavarasetting.util.novelListEng
import com.bigbigdw.manavarasetting.util.saveBook
import com.bigbigdw.manavarasetting.util.saveData
import com.bigbigdw.manavarasetting.util.saveKeyword
import com.bigbigdw.manavarasetting.util.saveKeywordData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    companion object {
        const val WORKER = "WORKER"
        const val PLATFORM = "PLATFORM"
        const val TYPE = "TYPE"
    }

    override fun doWork(): Result {

        val workerName =
            inputData.getString(WORKER) + "_" + inputData.getString(PLATFORM) + "_" + inputData.getString(
                TYPE
            )

        val year = DBDate.dateMMDDHHMMss().substring(0, 4)
        val month = DBDate.dateMMDDHHMMss().substring(4, 6)
        val day = DBDate.dateMMDDHHMMss().substring(6, 8)
        val hour = DBDate.dateMMDDHHMMss().substring(8, 10)
        val min = DBDate.dateMMDDHHMMss().substring(10, 12)
        val sec = DBDate.dateMMDDHHMMss().substring(12, 14)

        if(inputData.getString(WORKER)?.contains("BOOK") == true){
            for(platform in novelListEng()){
                runBlocking {
                    saveBook(
                        platform = platform,
                        type = inputData.getString(TYPE) ?: "",
                    )

//                    saveData(
//                        platform = platform,
//                        type = inputData.getString(TYPE) ?: "",
//                    )
//
//                    saveKeywordData(
//                        platform = platform,
//                        type = inputData.getString(TYPE) ?: "",
//                    )
                }
            }

            postFCM(
                data = "마나바라 BOOK 최신화 ${inputData.getString(TYPE)}",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "${inputData.getString(WORKER)} ${inputData.getString(TYPE)}",
            )

        } else if(inputData.getString(WORKER)?.contains("NOVEL") == true){

            for(platform in novelListEng()){
                runBlocking {
                    if (DBDate.getDayOfWeekAsNumber().toString() == "0") {
                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_WEEK").removeValue()

                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_WEEK_TOTAL").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_MONTH").removeValue()

                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_MONTH_TOTAL").removeValue()
                    }

                    MiningSource.mining(
                        platform = platform,
                        type = inputData.getString(TYPE) ?: "",
                        context = applicationContext
                    )
                }

                Log.d("MINING-TEST", "PLATFORM == $platform")

                Thread.sleep(500)
            }

            postFCM(
                data = "마나바라 베스트 최신화 ${inputData.getString(TYPE)}",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "NOVEL",
            )

        } else if(inputData.getString(WORKER)?.contains("TEST") == true){

            val list = if (inputData.getString(PLATFORM)?.contains("JOARA") == true) {
                arrayListOf("JOARA", "JOARA_NOBLESS", "JOARA_PREMIUM")
            } else if (inputData.getString(PLATFORM)?.contains("NAVER") == true) {
                arrayListOf(
                    "NAVER_WEBNOVEL_FREE",
                    "NAVER_WEBNOVEL_PAY",
                    "NAVER_BEST",
                    "NAVER_CHALLENGE"
                )
            } else if (inputData.getString(PLATFORM)?.contains("RIDI") == true) {
                arrayListOf(
                    "RIDI_FANTAGY",
                    "RIDI_ROMANCE",
                    "RIDI_ROFAN"
                )
            } else if (inputData.getString(PLATFORM)?.contains("ONESTORY") == true) {
                arrayListOf(
                    "ONESTORY_FANTAGY",
                    "ONESTORY_ROMANCE",
                    "ONESTORY_PASS_FANTAGY",
                    "ONESTORY_PASS_ROMANCE"
                )
            } else if (inputData.getString(PLATFORM)?.contains("TOKSODA") == true) {
                arrayListOf(
                    "TOKSODA",
                    "TOKSODA_FREE"
                )
            } else if (inputData.getString(PLATFORM)?.contains("NAVER_SERIES") == true) {
                arrayListOf(
                    "NAVER_SERIES",
                )
            } else if (inputData.getString(PLATFORM)?.contains("KAKAO_STAGE") == true) {
                arrayListOf(
                    "KAKAO_STAGE",
                )
            } else if (inputData.getString(PLATFORM)?.contains("MUNPIA") == true) {
                arrayListOf(
                    "MUNPIA_PAY",
                    "MUNPIA_FREE"
                )
            } else {
                arrayListOf("")
            }

            for(platform in list){
                runBlocking {
                    if (DBDate.getDayOfWeekAsNumber().toString() == "0") {
                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_WEEK").removeValue()

                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_WEEK_TOTAL").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_MONTH").removeValue()

                        BestRef.setBestRef(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        ).child("TROPHY_MONTH_TOTAL").removeValue()
                    }

                    runBlocking {
                        MiningSource.mining(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                            context = applicationContext
                        )
                    }

                    runBlocking {
                        saveBook(
                            platform = platform,
                            type = inputData.getString(TYPE) ?: "",
                        )
                    }
                }
            }

            postFCM(
                data = "마나바라 ${inputData.getString(TYPE)} ${inputData.getString(PLATFORM)} 최신화",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "NOVEL",
            )

        } else if(inputData.getString(WORKER)?.contains("MINING") == true){

            runBlocking {
                if (DBDate.getDayOfWeekAsNumber().toString() == "0") {
                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_WEEK").removeValue()

                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_WEEK_TOTAL").removeValue()
                }

                if (DBDate.datedd() == "01") {
                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_MONTH").removeValue()

                    BestRef.setBestRef(
                        platform = inputData.getString(PLATFORM) ?: "",
                        type = inputData.getString(TYPE) ?: "",
                    ).child("TROPHY_MONTH_TOTAL").removeValue()
                }

                MiningSource.mining(
                    platform = inputData.getString(PLATFORM) ?: "",
                    type = inputData.getString(TYPE) ?: "",
                    context = applicationContext
                )
            }

            runBlocking {
                saveBook(
                    platform = inputData.getString(PLATFORM) ?: "",
                    type = inputData.getString(TYPE) ?: "",
                )

                saveData(
                    platform = inputData.getString(PLATFORM) ?: "",
                    type = inputData.getString(TYPE) ?: "",
                )
            }

            runBlocking {
                val workManager = WorkManager.getInstance(applicationContext)

                Log.d("MINING", "getNextNovelInListEng(inputData.getString(PLATFORM) ?: \"\") == ${getNextNovelInListEng(inputData.getString(PLATFORM) ?: "")}")

                if (getNextNovelInListEng(inputData.getString(PLATFORM) ?: "").isNotEmpty()) {
                    MiningWorker.doWorkerOnetime(
                        workManager = workManager,
                        time = 15,
                        timeUnit = TimeUnit.MINUTES,
                        tag = "MINING",
                        platform = getNextNovelInListEng(inputData.getString(PLATFORM) ?: ""),
                        type = "NOVEL"
                    )
                } else {
                    MiningWorker.doWorkerOnetime(
                        workManager = workManager,
                        time = 12,
                        timeUnit = TimeUnit.HOURS,
                        tag = "MINING",
                        platform = "JOARA",
                        type = "NOVEL"
                    )
                }
            }

            postFCM(
                data = "마나바라 ${inputData.getString(PLATFORM)} 베스트 최신화",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "NOVEL",
            )

        }  else {

            postFCM(
                data = workerName,
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "${inputData.getString(PLATFORM)} ${inputData.getString(TYPE)}",
            )
        }

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

        miningTime()

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

    private fun miningTime() {
        FirebaseDatabase.getInstance().reference.child("MINING").setValue(DBDate.dateMMDDHHMM())
    }
}