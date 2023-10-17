package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.novelListEng
import com.bigbigdw.manavarasetting.util.saveBook
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
                }
            }

            postFCM(
                data = workerName,
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "${inputData.getString(WORKER)} ${inputData.getString(TYPE)}",
            )

        } else if(workerName.contains("MINING")){
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
            }

            postFCM(
                data = "마나바라 웹소설",
                time = "${year}.${month}.${day} ${hour}:${min}:${sec}",
                activity = "NOVEL",
            )

        } else {

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