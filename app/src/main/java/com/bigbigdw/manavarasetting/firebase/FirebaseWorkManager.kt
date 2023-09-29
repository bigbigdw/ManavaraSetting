package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.setDataStore
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageMonth
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageTrophy
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    val dataStore = DataStoreManager(context)

    companion object {
        const val TYPE = "type"
        const val UID = "uid"
        const val USER = "user"
    }

    override fun doWork(): Result {

        val year = DBDate.dateMMDDHHMM().substring(0,4)
        val month = DBDate.dateMMDDHHMM().substring(4,6)
        val day = DBDate.dateMMDDHHMM().substring(6,8)
        val hour = DBDate.dateMMDDHHMM().substring(8,10)
        val min = DBDate.dateMMDDHHMM().substring(10,12)

        if (inputData.getString(TYPE).equals("BEST")) {

            for (j in NaverSeriesGenre) {

                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_WEEK").removeValue()
                }

                if (DBDate.datedd() == "01") {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_MONTH").removeValue()
                }


                for (i in 1..5) {
                    Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                }
            }

            postFCM(data = "베스트 리스트가 갱신되었습니다", time = "${year}.${month}.${day} ${hour}:${min}")

        } else if (inputData.getString(TYPE).equals("JSON")) {
            for (j in NaverSeriesGenre) {

                uploadJsonArrayToStorageDay(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )

                uploadJsonArrayToStorageWeek(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )

                uploadJsonArrayToStorageMonth(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )

                uploadJsonArrayToStorageTrophy(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "월간"
                )
            }

            postFCM(data = "JSON 최신화가 완료되었습니다", time = "${year}.${month}.${day} ${hour}:${min}")

        } else if (inputData.getString(TYPE).equals("TROPHY")) {

            for (j in NaverSeriesGenre) {
                calculateTrophy(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
            }

            postFCM(data = "트로피 정산이 완료되었습니다", time = "${year}.${month}.${day} ${hour}:${min}")

        } else if (inputData.getString(TYPE).equals("TEST")) {
            postFCM(data = "테스트", time = "${year}.${month}.${day} ${hour}:${min}")

        }

        return Result.success()
    }

    private fun postFCM(data : String, time : String) {

        val fcmBody = DataFCMBody(
            "/topics/adminAll",
            "high",
            DataFCMBodyData("마나바라 세팅", data),
            DataFCMBodyNotification(title = "마나바라 세팅", body = "$time $data", click_action= "best"),
        )

        miningAlert("마나바라 세팅", "$time $data", "ALERT")

        setDataStore(message = data, context = applicationContext)

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

    private fun miningAlert(title: String, message: String, child: String) {
        FirebaseDatabase.getInstance().reference.child("MESSAGE").child(child)
            .child(DBDate.dateMMDDHHMM()).setValue(
                FCMAlert(
                    DBDate.dateMMDDHHMM(),
                    title, message
                )
            )
    }
}