package com.bigbigdw.manavarasetting.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bigbigdw.manavarasetting.R

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingOnReceived : FirebaseMessagingService() {

    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null
    var it = ""

    override fun onNewToken(token: String) {
        Log.d("TEST", "Refreshed token: $token")

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.notification?.title ?: ""
        val message = remoteMessage.notification?.body ?: ""

        if (remoteMessage.notification != null) {
            showNotification(
                title = title,
                message = message,
            )
        }
    }

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