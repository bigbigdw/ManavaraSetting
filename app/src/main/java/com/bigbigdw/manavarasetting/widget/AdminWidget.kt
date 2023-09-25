package com.bigbigdw.manavarasetting.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentSize
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.firebase.FCMAlert
import com.bigbigdw.manavarasetting.main.event.EventMain
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.BESTWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_BEST
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_BEST_TODAY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_JSON
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_JSON_TODAY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_TEST
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_TEST_TODAY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_TROPHY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.FCM_COUNT_TROPHY_TODAY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.JSONWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TEST_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TROPHYWORKER_TIME
import com.bigbigdw.manavarasetting.ui.theme.color4186e1
import com.bigbigdw.manavarasetting.ui.theme.colorB3000000
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerInterval
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerStatus
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTag
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTimeMill
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class AdminWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = ManavaraSettingWidget
}

object ManavaraSettingWidget : GlanceAppWidget() {

    val countKey = intPreferencesKey("count")

    val worker = stringPreferencesKey("worker")
    val paramWorkerStatus = ActionParameters.Key<String>("WOKER_STATUS")
    val paramWorkerTag = ActionParameters.Key<String>("WOKER_TAG")
    val paramWorkerInterval = ActionParameters.Key<Long>("WOKER_INTERVAL")
    val paramWorkerTimeMill = ActionParameters.Key<TimeUnit>("WOKER_TIMEMILL")

    val test = stringPreferencesKey("TEST")

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            ScreenWidget(context = context)
        }
    }
}

@Composable
fun ScreenWidget(context: Context) {

    val dataStore = DataStoreManager(context)

    val TEST_TIME = dataStore.getDataStoreString(TEST_TIME).collectAsState(initial = "")
    val BESTWORKER_TIME = dataStore.getDataStoreString(BESTWORKER_TIME).collectAsState(initial = "")
    val JSONWORKER_TIME = dataStore.getDataStoreString(JSONWORKER_TIME).collectAsState(initial = "")
    val TROPHYWORKER_TIME = dataStore.getDataStoreString(TROPHYWORKER_TIME).collectAsState(initial = "")

    val FCM_COUNT_TEST = dataStore.getDataStoreString(FCM_COUNT_TEST).collectAsState(initial = "")
    val FCM_COUNT_TEST_TODAY = dataStore.getDataStoreString(FCM_COUNT_TEST_TODAY).collectAsState(initial = "")

    val FCM_COUNT_BEST = dataStore.getDataStoreString(FCM_COUNT_BEST).collectAsState(initial = "")
    val FCM_COUNT_BEST_TODAY = dataStore.getDataStoreString(FCM_COUNT_BEST_TODAY).collectAsState(initial = "")

    val FCM_COUNT_JSON = dataStore.getDataStoreString(FCM_COUNT_JSON).collectAsState(initial = "")
    val FCM_COUNT_JSON_TODAY = dataStore.getDataStoreString(FCM_COUNT_JSON_TODAY).collectAsState(initial = "")

    val FCM_COUNT_TROPHY = dataStore.getDataStoreString(FCM_COUNT_TROPHY).collectAsState(initial = "")
    val FCM_COUNT_TROPHY_TODAY = dataStore.getDataStoreString(FCM_COUNT_TROPHY_TODAY).collectAsState(initial = "")

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = colorB3000000)
            .padding(16.dp)
            .clickable(actionRunCallback(callbackClass = WidgetUpdate::class.java)),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        Spacer(modifier = GlanceModifier.size(12.dp))

        Row(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                modifier = GlanceModifier.size(28.dp),
                provider = ImageProvider(R.drawable.ic_launcher),
                contentDescription = "Image"
            )

            Spacer(modifier = GlanceModifier.size(8.dp))

            Text(
                text = "세팅바라 현황",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 20.sp,
                )
            )
        }

        Spacer(modifier = GlanceModifier.size(8.dp))

        Column(
            modifier = GlanceModifier
                .wrapContentSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            ItemMainSetting(
                image = R.drawable.icon_setting_wht,
                titleWorker = "테스트 WORKER : ",
                valueWorker = TEST_TIME.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_setting_wht,
                titleWorker = "테스트 호출 횟수 : ",
                valueWorker = FCM_COUNT_TEST.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_setting_wht,
                titleWorker = "테스트 금일 호출 횟수 : ",
                valueWorker = FCM_COUNT_TEST_TODAY.value ?: "알 수 없음",
            )

        }

        Column(
            modifier = GlanceModifier
                .wrapContentSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            ItemMainSetting(
                image = R.drawable.icon_best_wht,
                titleWorker = "베스트 WORKER : ",
                valueWorker = BESTWORKER_TIME.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_best_wht,
                titleWorker = "베스트 호출 횟수 : ",
                valueWorker = FCM_COUNT_BEST.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_best_wht,
                titleWorker = "베스트 금일 호출 횟수 : ",
                valueWorker = FCM_COUNT_BEST_TODAY.value ?: "알 수 없음",
            )

        }

        Column(
            modifier = GlanceModifier
                .wrapContentSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            ItemMainSetting(
                image = R.drawable.icon_json_wht,
                titleWorker = "JSON WORKER : ",
                valueWorker = JSONWORKER_TIME.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_json_wht,
                titleWorker = "JSON 호출 횟수 : ",
                valueWorker = FCM_COUNT_JSON.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_json_wht,
                titleWorker = "JSON 금일 호출 횟수 : ",
                valueWorker = FCM_COUNT_JSON_TODAY.value ?: "알 수 없음",
            )

        }

        Column(
            modifier = GlanceModifier
                .wrapContentSize()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            ItemMainSetting(
                image = R.drawable.icon_trophy_wht,
                titleWorker = "트로피 WORKER : ",
                valueWorker = TROPHYWORKER_TIME.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_trophy_wht,
                titleWorker = "트로피 호출 횟수 : ",
                valueWorker = FCM_COUNT_TROPHY.value ?: "알 수 없음",
            )

            ItemMainSetting(
                image = R.drawable.icon_trophy_wht,
                titleWorker = "트로피 금일 호출 횟수 : ",
                valueWorker = FCM_COUNT_TROPHY_TODAY.value ?: "알 수 없음",
            )

            Spacer(modifier = GlanceModifier.size(8.dp))
        }
    }
}

@Composable
fun ItemMainSetting(
    image: Int,
    titleWorker: String,
    valueWorker: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Image(
            modifier = GlanceModifier.size(16.dp),
            provider = ImageProvider(image),
            contentDescription = "Image"
        )

        Spacer(modifier = GlanceModifier.size(4.dp))

        Text(
            text = titleWorker,
            style = TextStyle(
                color = ColorProvider(Color.White),
                fontSize = 18.sp,
            )
        )

        Spacer(modifier = GlanceModifier.size(4.dp))

        Text(
            text = valueWorker,
            style = TextStyle(
                color = ColorProvider(color4186e1),
                fontSize = 18.sp,
            )
        )
    }

    Spacer(modifier = GlanceModifier.size(4.dp))
}

class WidgetUpdate : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        updateWorker(context = context, update = ManavaraSettingWidget.update(context, glanceId))

        updateFcmCount(context = context, update = ManavaraSettingWidget.update(context, glanceId))
    }
}

fun updateFcmCount(context: Context, update : Unit){
    val mRootRef = FirebaseDatabase.getInstance().reference.child("MESSAGE").child("ALERT")

    val year = DBDate.dateMMDDHHMM().substring(0,4)
    val month = DBDate.dateMMDDHHMM().substring(4,6)
    val day = DBDate.dateMMDDHHMM().substring(6,8)

    var numFcm = 0
    var numFcmToday = 0
    var numBest = 0
    var numBestToday = 0
    var numJson = 0
    var numJsonToday = 0
    var numTrophy = 0
    var numTrophyToday = 0

    val dataStore = DataStoreManager(context)

    mRootRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if(dataSnapshot.exists()){

                for(item in dataSnapshot.children){
                    val fcm: FCMAlert? = dataSnapshot.child(item.key ?: "").getValue(FCMAlert::class.java)

                    if(fcm?.body?.contains("테스트") == true){
                        numFcm += 1

                        if(fcm.body.contains("${year}.${month}.${day}")){
                            numFcmToday += 1
                        }
                    } else if (fcm?.body?.contains("베스트 리스트가 갱신되었습니다") == true) {
                        numBest += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numBestToday += 1
                        }
                    } else if (fcm?.body?.contains("DAY JSON 생성이 완료되었습니다") == true) {
                        numJson += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numJsonToday += 1
                        }
                    } else if (fcm?.body?.contains("트로피 정산이 완료되었습니다") == true) {
                        numTrophy += 1

                        if (fcm.body.contains("${year}.${month}.${day}")) {
                            numTrophyToday += 1
                        }
                    } else {
                        Log.d("HIHIHIHI", "item = $item")
                    }

                }

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(FCM_COUNT_TEST, numFcm.toString())
                    dataStore.setDataStoreString(FCM_COUNT_TEST_TODAY, numFcmToday.toString())
                    dataStore.setDataStoreString(FCM_COUNT_BEST, numBest.toString())
                    dataStore.setDataStoreString(FCM_COUNT_BEST_TODAY, numBestToday.toString())
                    dataStore.setDataStoreString(FCM_COUNT_JSON, numJson.toString())
                    dataStore.setDataStoreString(FCM_COUNT_JSON_TODAY, numJsonToday.toString())
                    dataStore.setDataStoreString(FCM_COUNT_TROPHY, numTrophy.toString())
                    dataStore.setDataStoreString(FCM_COUNT_TROPHY_TODAY, numTrophyToday.toString())
                    update
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}
fun updateWorker(context: Context, update : Unit){
    val workerRef = FirebaseDatabase.getInstance().reference.child("WORKER")

    workerRef.addListenerForSingleValueEvent(object :
        ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val dataStore = DataStoreManager(context)

                val workerTest: String? = dataSnapshot.child("WORKER_TEST").getValue(String::class.java)
                val workerBest: String? = dataSnapshot.child("WORKER_BEST").getValue(String::class.java)
                val workerJson: String? = dataSnapshot.child("WORKER_JSON").getValue(String::class.java)
                val workerTrophy: String? = dataSnapshot.child("WORKER_TROPHY").getValue(String::class.java)

                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.setDataStoreString(TEST_TIME, workerTest ?: "")
                    dataStore.setDataStoreString(BESTWORKER_TIME, workerBest ?: "")
                    dataStore.setDataStoreString(JSONWORKER_TIME, workerJson ?: "")
                    dataStore.setDataStoreString(TROPHYWORKER_TIME, workerTrophy ?: "")
                    update
                }

            } else {
                Log.d("HIHI", "FALSE")
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {}
    })
}

class WidgetCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        val workManager = WorkManager.getInstance(context)

        updateAppWidgetState(context, glanceId) { prefs ->

            if (parameters[paramWorkerStatus] == "DO") {
                PeriodicWorker.doWorker(
                    workManager = workManager,
                    repeatInterval = parameters[paramWorkerInterval] ?: 0,
                    tag = parameters[paramWorkerTag] ?: "",
                    timeMill = parameters[paramWorkerTimeMill] ?: TimeUnit.HOURS
                )

            } else if (parameters[paramWorkerStatus] == "CANCEL") {
                PeriodicWorker.cancelWorker(
                    workManager = workManager,
                    tag = "TEST"
                )

            }

            if (parameters[paramWorkerTag] != null) {
                val status = withContext(Dispatchers.IO) {
                    workManager.getWorkInfosByTag("TEST").get()
                }

                val comment = if (status.isEmpty()) {
                    "활성화 되지 않음"
                } else {
                    status[0].state.name
                }

                prefs[ManavaraSettingWidget.worker] = comment
            } else {
                prefs[ManavaraSettingWidget.worker] = "활성화 되지 않음"
            }
        }

        ManavaraSettingWidget.update(context, glanceId)
    }
}