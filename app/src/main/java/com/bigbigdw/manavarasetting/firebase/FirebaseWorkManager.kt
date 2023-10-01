package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.setDataStore
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    val dataStore = DataStoreManager(context)

    companion object {
        const val WORKER = "WORKER"
        const val PLATFORM = "PLATFORM"
    }

    override fun doWork(): Result {

        val year = DBDate.dateMMDDHHMM().substring(0, 4)
        val month = DBDate.dateMMDDHHMM().substring(4, 6)
        val day = DBDate.dateMMDDHHMM().substring(6, 8)
        val hour = DBDate.dateMMDDHHMM().substring(8, 10)
        val min = DBDate.dateMMDDHHMM().substring(10, 12)

        var workerName = inputData.getString(WORKER) + "_" + inputData.getString(PLATFORM)

        if (inputData.getString(WORKER).equals("BEST")) {

            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_WEEK").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    repeat(5) { i ->
                        launch(threadPool) {
                            MiningSource.miningNaverSeriesComic(pageCount = i + 1, genre = j)
                        }
                    }
                }
            }

            threadPool.close()

            postFCM(
                data = "베스트 리스트가 갱신되었습니다",
                time = "${year}.${month}.${day} ${hour}:${min}",
                activity = "BEST"
            )

        } else if (inputData.getString(WORKER).equals("JSON")) {
            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    launch {
                        uploadJsonArrayToStorageDay(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "COMIC"
                        )
                    }

                }
            }

            postFCM(
                data = "JSON 투데이 최신화가 완료되었습니다",
                time = "${year}.${month}.${day} ${hour}:${min}",
                activity = "JSON"
            )

        } else if (inputData.getString(WORKER).equals("TROPHY")) {

            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    launch {
                        calculateTrophy(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "COMIC"
                        )
                    }

                }
            }

            postFCM(
                data = "트로피 정산이 완료되었습니다",
                time = "${year}.${month}.${day} ${hour}:${min}",
                activity = "TROPHY"
            )

        } else if (inputData.getString(WORKER).equals("TEST")) {
            postFCM(data = "테스트", time = "${year}.${month}.${day} ${hour}:${min}")

        }

        return Result.success()
    }

    private fun postFCM(data: String, time: String, activity: String = "") {

        val fcmBody = DataFCMBody(
            "/topics/adminAll",
            "high",
            DataFCMBodyData("마나바라 세팅", data),
            DataFCMBodyNotification(title = "마나바라 세팅", body = "$time $data", click_action = "best"),
        )

        miningAlert("마나바라 세팅", "$time $data", "ALERT", activity = activity)

        setDataStore(activity = activity)

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

    private fun miningAlert(title: String, message: String, child: String, activity: String = "") {
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