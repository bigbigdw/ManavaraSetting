package com.bigbigdw.manavarasetting.main.viewModels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbigdw.manavarasetting.firebase.DataFCMBody
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyData
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.firebase.FCM
import com.bigbigdw.manavarasetting.firebase.FWorkManagerResult
import com.bigbigdw.manavarasetting.firebase.FirebaseService
import com.bigbigdw.manavarasetting.main.ActivityMain
import com.bigbigdw.manavarasetting.main.event.EventFCM
import com.bigbigdw.manavarasetting.main.event.StateFCM
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

    fun postFCMAlert(context: Context) {

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