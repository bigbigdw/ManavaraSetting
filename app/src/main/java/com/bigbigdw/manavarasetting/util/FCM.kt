package com.bigbigdw.manavarasetting.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.bigbigdw.manavarasetting.firebase.DataFCMBody
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyData
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.firebase.FWorkManagerResult
import com.bigbigdw.manavarasetting.util.DBDate.dateMMDDHHMM
import com.bigbigdw.manavarasetting.util.DataStoreManager.Companion.FCM_TOKEN
import com.bigbigdw.manavarasetting.firebase.FirebaseService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FCM {

    fun postFCMAlertTest(context: Context, message : String) {

        val year = DBDate.dateMMDDHHMM().substring(0,4)
        val month = DBDate.dateMMDDHHMM().substring(4,6)
        val day = DBDate.dateMMDDHHMM().substring(6,8)
        val hour = DBDate.dateMMDDHHMM().substring(8,10)
        val min = DBDate.dateMMDDHHMM().substring(10,12)

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("ALERT_ALL", ""),
            DataFCMBodyNotification(
                "공지사항",
                "${year}.${month}.${day} ${hour}:${min} $message",
                ""
            ),
        )

        postFCM(context = context, fcmBody = fcmBody)

        setDataStore(data = "TEST")

        miningAlert(title = "공지사항", message = "${year}.${month}.${day} ${hour}:${min} $message")
    }

    private fun miningAlert(
        title: String,
        message: String,
        activity: String = "",
        data: String = ""
    ) {

        FirebaseDatabase.getInstance().reference.child("MESSAGE").child("NOTICE").child(dateMMDDHHMM()).setValue(
            FCMAlert(dateMMDDHHMM(), title, message, data = data, activity = activity)
        )
    }

    private fun postFCM(context : Context, fcmBody : DataFCMBody) {

        val fcm = Intent(context.applicationContext, FirebaseMessaging::class.java)
        context.startService(fcm)

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

                    }
                } else {

                }
            }

            override fun onFailure(call: Call<FWorkManagerResult?>, t: Throwable) {

            }
        })
    }

    fun getFCMToken(context: Context){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("ViewModelFCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast

            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()

            val dataStore = DataStoreManager(context)

            CoroutineScope(Dispatchers.IO).launch {
                dataStore.setDataStoreString(key = FCM_TOKEN, str = token)
            }
        })
    }

    fun postFCMAlert(context: Context, getFCM : DataFCMBodyNotification) {

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("ALERT_ALL", ""),
            DataFCMBodyNotification(
                getFCM.title,
                getFCM.body,
                ""
            ),
        )

        postFCM(context = context, fcmBody = fcmBody)

        setDataStore(data = "NOTICE")

        miningAlert(title = getFCM.title, message = getFCM.body)
    }
}