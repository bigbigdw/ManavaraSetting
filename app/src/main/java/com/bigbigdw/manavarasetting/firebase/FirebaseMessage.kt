package com.bigbigdw.manavarasetting.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseMessage : FirebaseMessagingService() {

    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null
    var it = ""

    override fun onNewToken(token: String) {
        Log.d("TEST", "Refreshed token: $token")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.notification?.title ?: ""
        val message = remoteMessage.notification?.body ?: ""

        if (remoteMessage.notification != null) {
            showNotification(
                title = title,
                message = message,
            )
            setDataStore(message = message)
        }
    }

    private fun setDataStore(message: String){
        val dataStore = DataStoreManager(applicationContext)
        val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

        if(message.contains("위젯 테스트")){
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.setDataStoreString(key = DataStoreManager.TEST_TIME, str = message.replace(" 위젯 테스트",""))
            }

            mRootRef.child("TEST_TIME").setValue(message.replace(" 위젯 테스트",""))

        } else if(message.contains(" 트로피 정산이 완료되었습니다")){
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.setDataStoreString(key = DataStoreManager.TROPHYWORKER_TIME, str = message.replace(" 트로피 정산이 완료되었습니다",""))
            }

            mRootRef.child("TROPHYWORKER_TIME").setValue(message.replace(" 트로피 정산이 완료되었습니다",""))

        } else if(message.contains(" DAY JSON 생성이 완료되었습니다")){
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.setDataStoreString(key = DataStoreManager.JSONWORKER_TIME, str = message.replace(" DAY JSON 생성이 완료되었습니다",""))
            }

            mRootRef.child("JSONWORKER_TIME").setValue(message.replace(" DAY JSON 생성이 완료되었습니다",""))

        } else if(message.contains(" 베스트 리스트가 갱신되었습니다")){
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.setDataStoreString(key = DataStoreManager.BESTWORKER_TIME, str = message.replace(" 베스트 리스트가 갱신되었습니다",""))
            }

            mRootRef.child("BESTWORKER_TIME").setValue(message.replace(" 베스트 리스트가 갱신되었습니다",""))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(title: String, message: String) {

//        if(activity.isNotEmpty()){
//            miningUserAlert(title, message, "Alert", applicationContext, activity = activity, data = data)
//        }

        // NotificationManager 객체 생성
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "공지사항"
        val channelName = "공지사항"
        val channelDescription = "공지사항"

        val notificationChannel: NotificationChannel?
        notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

        notificationChannel.description = channelDescription
        notificationManager?.createNotificationChannel(notificationChannel)

        notificationBuilder = NotificationCompat.Builder(this, channelId)

        // Builder의 setter를 사용하여 Notification 설정
        notificationBuilder?.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder?.setContentTitle(title)
        notificationBuilder?.setContentText(message)
        notificationBuilder?.setAutoCancel(true)

//        val intent = if(activity.equals("CLICK_STUDENT_SUBMIT")){
//            Intent(this, ActivityAgreeStudent::class.java)
//        } else {
//            Intent(this, ActivityMain::class.java)
//        }
//
//        try {
//
//            if(activity.equals("CLICK_STUDENT_SUBMIT")){
//                val dataJson = JSONObject(data)
//
//                intent.putExtra("parentUID", dataJson.optString("parentUID"))
//                intent.putExtra("parentEmail", dataJson.optString("parentEmail"))
//                intent.putExtra("studentEmail", dataJson.optString("studentEmail"))
//
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//
//        } catch (e : Exception){}
//
//        // 시스템에게 의뢰할 Intent를 담는 클래스
//        val activityPendingIntent = PendingIntent.getActivity(applicationContext, 10, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // 사용자가 알림을 클릭 시 이동할 PendingIntent
//        notificationBuilder?.setContentIntent(activityPendingIntent)

        // NotificationManger.nofity를 이용하여 Notification post
        notificationManager?.notify(0, notificationBuilder?.build())
    }

}