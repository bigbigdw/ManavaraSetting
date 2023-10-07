package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.JoaraGenre
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.NaverSeriesNovelGenre
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getJoaraGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.setDataStore
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

        if (inputData.getString(WORKER).equals("BEST")) {

            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {

                if (inputData.getString(PLATFORM).equals("NAVER_SERIES")) {

                    if (inputData.getString(PLATFORM).equals("COMIC")) {

                        for (j in NaverSeriesComicGenre) {
                            if (DBDate.getDayOfWeekAsNumber() == 0) {
                                BestRef.setBestRef(
                                    platform = "NAVER_SERIES",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: ""
                                )
                                    .child("TROPHY_WEEK").removeValue()
                            }

                            if (DBDate.datedd() == "01") {
                                BestRef.setBestRef(
                                    platform = "NAVER_SERIES",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: ""
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            repeat(5) { i ->
                                launch(threadPool) {
                                    MiningSource.miningNaverSeriesComic(
                                        pageCount = i + 1,
                                        genre = j
                                    )
                                }
                            }
                        }

                    } else {
                        for (j in NaverSeriesNovelGenre) {
                            if (DBDate.getDayOfWeekAsNumber() == 0) {
                                BestRef.setBestRef(
                                    platform = "NAVER_SERIES",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: ""
                                )
                                    .child("TROPHY_WEEK").removeValue()
                            }

                            if (DBDate.datedd() == "01") {
                                BestRef.setBestRef(
                                    platform = "NAVER_SERIES",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: ""
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            repeat(5) { i ->
                                launch(threadPool) {
                                    MiningSource.miningNaverSeriesNovel(
                                        pageCount = i + 1,
                                        genre = j
                                    )
                                }
                            }
                        }
                    }

                }

            }

            threadPool.close()

        } else if (inputData.getString(WORKER).equals("JSON")) {

            if (inputData.getString(PLATFORM).equals("NAVER_SERIES")) {

                runBlocking {
                    if (inputData.getString(PLATFORM).equals("COMIC")) {

                        for (j in NaverSeriesComicGenre) {
                            launch {
                                uploadJsonArrayToStorageDay(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = getNaverSeriesGenre(j),
                                    type = inputData.getString(TYPE) ?: ""
                                )
                            }

                        }

                    } else {
                        for (j in NaverSeriesNovelGenre) {
                            launch {
                                uploadJsonArrayToStorageDay(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = getNaverSeriesGenre(j),
                                    type = inputData.getString(TYPE) ?: ""
                                )
                            }

                        }
                    }
                }

            }

        } else if (inputData.getString(WORKER).equals("TROPHY")) {

            if (inputData.getString(PLATFORM).equals("NAVER_SERIES")) {

                runBlocking {
                    if (inputData.getString(PLATFORM).equals("COMIC")) {

                        for (j in NaverSeriesComicGenre) {
                            launch {
                                calculateTrophy(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = getNaverSeriesGenre(j),
                                    type = inputData.getString(TYPE) ?: ""
                                )
                            }

                        }

                    } else {
                        for (j in NaverSeriesNovelGenre) {
                            launch {
                                calculateTrophy(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = getNaverSeriesGenre(j),
                                    type = inputData.getString(TYPE) ?: ""
                                )
                            }

                        }
                    }
                }

            }

        } else if (inputData.getString(WORKER).equals("TEST")) {
            postFCM(data = "테스트", time = "${year}.${month}.${day} ${hour}:${min}")

        } else if (inputData.getString(WORKER)?.contains("MINING_COMIC") == true) {

            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = inputData.getString(TYPE) ?: ""
                        )
                            .child("TROPHY_WEEK").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = inputData.getString(TYPE) ?: ""
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    val miningJobs = List(5) { i ->
                        async(threadPool) {
                            MiningSource.miningNaverSeriesComic(pageCount = i + 1, genre = j)
                        }
                    }

                    miningJobs.awaitAll()

                    launch {
                        uploadJsonArrayToStorageDay(
                            platform = inputData.getString(PLATFORM) ?: "",
                            genre = getNaverSeriesGenre(j),
                            type = inputData.getString(TYPE) ?: ""
                        )
                    }

                    launch {
                        calculateTrophy(
                            platform = inputData.getString(PLATFORM) ?: "",
                            genre = getNaverSeriesGenre(j),
                            type = inputData.getString(TYPE) ?: ""
                        )
                    }
                }
            }
        } else if (inputData.getString(WORKER)?.contains("MINING_NOVEL") == true) {

            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {
                for (j in NaverSeriesNovelGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = inputData.getString(TYPE) ?: ""
                        )
                            .child("TROPHY_WEEK").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = inputData.getString(TYPE) ?: ""
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    val miningJobs = List(5) { i ->
                        async(threadPool) {
                            MiningSource.miningNaverSeriesNovel(pageCount = i + 1, genre = j)
                        }
                    }

                    miningJobs.awaitAll()

                    launch {
                        uploadJsonArrayToStorageDay(
                            platform = inputData.getString(PLATFORM) ?: "",
                            genre = getNaverSeriesGenre(j),
                            type = inputData.getString(TYPE) ?: ""
                        )
                    }

                    launch {
                        calculateTrophy(
                            platform = inputData.getString(PLATFORM) ?: "",
                            genre = getNaverSeriesGenre(j),
                            type = inputData.getString(TYPE) ?: ""
                        )
                    }
                }
            }
        } else if (inputData.getString(WORKER) == "MINING") {

            if (inputData.getString(PLATFORM) == "NAVER_SERIES") {
                if (inputData.getString(TYPE) == "COMIC") {
                    runBlocking {
                        for (j in NaverSeriesComicGenre) {
                            if (DBDate.getDayOfWeekAsNumber() == 0) {
                                BestRef.setBestRef(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: "",
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            if (DBDate.datedd() == "01") {
                                BestRef.setBestRef(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: "",
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            MiningSource.mining(
                                genre = j,
                                platform = inputData.getString(PLATFORM) ?: "",
                                type = inputData.getString(TYPE) ?: "",
                                genreDir = getNaverSeriesGenre(j),
                                context = applicationContext
                            )
                        }
                    }
                } else {
                    runBlocking {
                        for (j in NaverSeriesNovelGenre) {
                            if (DBDate.getDayOfWeekAsNumber() == 0) {
                                BestRef.setBestRef(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: "",
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            if (DBDate.datedd() == "01") {
                                BestRef.setBestRef(
                                    platform = inputData.getString(PLATFORM) ?: "",
                                    genre = j,
                                    type = inputData.getString(TYPE) ?: "",
                                )
                                    .child("TROPHY_MONTH").removeValue()
                            }

                            MiningSource.mining(
                                genre = j,
                                platform = inputData.getString(PLATFORM) ?: "",
                                type = inputData.getString(TYPE) ?: "",
                                genreDir = getNaverSeriesGenre(j),
                                context = applicationContext
                            )
                        }
                    }
                }
            } else if (inputData.getString(PLATFORM)?.contains("JOARA") == true) {
                runBlocking {
                    for (j in JoaraGenre) {
                        if (DBDate.getDayOfWeekAsNumber() == 0) {
                            BestRef.setBestRef(
                                platform = inputData.getString(PLATFORM) ?: "",
                                genre = j,
                                type = inputData.getString(TYPE) ?: "",
                            ).child("TROPHY_MONTH").removeValue()
                        }

                        if (DBDate.datedd() == "01") {
                            BestRef.setBestRef(
                                platform = inputData.getString(PLATFORM) ?: "",
                                genre = j,
                                type = inputData.getString(TYPE) ?: "",
                            ).child("TROPHY_MONTH").removeValue()
                        }

                        MiningSource.mining(
                            genre = j,
                            platform = inputData.getString(PLATFORM) ?: "",
                            type = inputData.getString(TYPE) ?: "",
                            genreDir = getJoaraGenre(j),
                            context = applicationContext
                        )
                    }
                }
            }
        }


        postFCM(
            data = workerName,
            time = "${year}.${month}.${day} ${hour}:${min}",
            activity = inputData.getString(WORKER) ?: "",
        )

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

        setDataStore(data = data)

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