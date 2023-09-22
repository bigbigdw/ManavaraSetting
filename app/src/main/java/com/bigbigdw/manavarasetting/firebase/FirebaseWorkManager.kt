package com.bigbigdw.manavarasetting.firebase

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FirebaseWorkManager(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

//    var userDao: DBUser? = null
//    var UserInfo : DataBaseUser? = null

    companion object {
        const val TYPE = "type"
        const val UID = "uid"
        const val USER = "user"
    }

    override fun doWork(): Result {

        Log.d("HIHIHIHI", "DO_WORK")

        if (inputData.getString(TYPE).equals("BEST")) {

            for (j in NaverSeriesGenre) {
                for (i in 1..5) {
                    Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                }
            }

            postFCM(data = "베스트 리스트가 갱신되었습니다")
        } else if (inputData.getString(TYPE).equals("BEST_JSON")) {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageDay(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )
                uploadJsonArrayToStorageWeek(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )
            }
            postFCM(data = "DAY JSON 생성이 완료되었습니다")
        } else if (inputData.getString(TYPE).equals("BEST_TROPHY")) {
            for (j in NaverSeriesGenre) {
                calculateTrophy(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
            }
            postFCM(data = "트로피 정산이 완료되었습니다")
        } else if (inputData.getString(TYPE).equals("PICK")) {
//            Mining.getMyPickMining(applicationContext)
//            postFCMPick(inputData.getString(UID).toString(), inputData.getString(USER).toString())
        }  else if (inputData.getString(TYPE).equals("TEST")) {
            postFCM(data = "위젯 테스트")
        }

        return Result.success()
    }

    private fun postFCM(data : String) {

        val year = DBDate.dateMMDDHHMM().substring(0,4)
        val month = DBDate.dateMMDDHHMM().substring(4,6)
        val day = DBDate.dateMMDDHHMM().substring(6,8)
        val hour = DBDate.dateMMDDHHMM().substring(8,10)
        val min = DBDate.dateMMDDHHMM().substring(10,12)

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("마나바라 세팅", data),
            DataFCMBodyNotification(title = "마나바라 세팅", body = "${year}.${month}.${day} ${hour}:${min} $data", click_action= "best"),
        )

        miningAlert("마나바라 세팅", "${year}.${month}.${day} ${hour}:${min} 베스트 리스트가 갱신되었습니다", "ALERT")


//        userDao = Room.databaseBuilder(
//            applicationContext,
//            DBUser::class.java,
//            "UserInfo"
//        ).allowMainThreadQueries().build()
//
//        if(userDao?.daoUser() != null){
//            UserInfo = userDao?.daoUser()?.get()
//        }
//
//        mRootRef.child("User").child(UserInfo?.UID ?: "").child("isInit").setValue(true)

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
                    response.body()?.let { it ->
                        Log.d("FCM", "성공")

                        for (j in NaverSeriesGenre) {
                           uploadJsonArrayToStorageDay(
                                platform = "NAVER_SERIES",
                                genre = getNaverSeriesGenre(j)
                            )
                        }
                    }
                } else {
                    Log.d("FCM", "실패2")
                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {
                Log.d("FCM", "실패");
            }
        })
    }

    private fun postFCMPick(UID : String, User : String) {

//        userDao = Room.databaseBuilder(
//            applicationContext,
//            DBUser::class.java,
//            "UserInfo"
//        ).allowMainThreadQueries().build()
//
//        if(userDao?.daoUser() != null){
//            UserInfo = userDao?.daoUser()?.get()
//        }
//
//        mRootRef.child("User").child(UID).child("isInit").setValue(true)
//
//        val fcmBody = DataFCMBody(
//            "/topics/${UID}",
//            "high",
//            DataFCMBodyData("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다."),
//            DataFCMBodyNotification("모아바라 PICK 최신화", "${User}님의 마이픽 리스트가 최신화 되었습니다.", "default", "ic_stat_ic_notification"),
//        )
//
//        val call = Retrofit.Builder()
//            .baseUrl("https://fcm.googleapis.com")
//            .addConverterFactory(GsonConverterFactory.create()).build()
//            .create(FirebaseService::class.java)
//            .postRetrofit(
//                fcmBody
//            )
//
//        call.enqueue(object : Callback<FWorkManagerResult?> {
//            override fun onResponse(
//                call: Call<FWorkManagerResult?>,
//                response: retrofit2.Response<FWorkManagerResult?>
//            ) {
//                if (response.isSuccessful) {
//                    Mining.getMyPickMining(applicationContext)
//                }
//            }
//
//            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {}
//        })
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