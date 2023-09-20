package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavarasetting.util.dateMMDDHHMM
import com.bigbigdw.manavarasetting.firebase.DataFCMBody
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyData
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.firebase.FCM
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.firebase.FWorkManagerResult
import com.bigbigdw.manavarasetting.main.event.EventFCM
import com.bigbigdw.manavarasetting.main.event.StateFCM
import com.bigbigdw.massmath.Firebase.FirebaseService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ViewModelFCM @Inject constructor() : ViewModel() {

    private val events = Channel<EventFCM>()

    val state: StateFlow<StateFCM> = events.receiveAsFlow()
        .runningFold(StateFCM(), ::reduceState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, StateFCM())

    private val _sideEffects = Channel<String>()

    val sideEffects = _sideEffects.receiveAsFlow()

    private fun reduceState(current: StateFCM, event: EventFCM): StateFCM {
        return when(event){
            EventFCM.Loaded -> {
                current.copy(Loaded = true)
            }
        }
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
        miningAlert(title = getFCM.title, message = getFCM.body)
    }

    fun postFCMAlertTest(context: Context) {

        val fcmBody = DataFCMBody(
            "/topics/all",
            "high",
            DataFCMBodyData("ALERT_ALL", ""),
            DataFCMBodyNotification(
                "공지사항",
                "공지사항이라능",
                ""
            ),
        )

        postFCM(context = context, fcmBody = fcmBody)
        miningAlert(title = "공지사항", message = "공지사항이라능")
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

        val fcm = Intent(context.applicationContext, FCM::class.java)
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

}