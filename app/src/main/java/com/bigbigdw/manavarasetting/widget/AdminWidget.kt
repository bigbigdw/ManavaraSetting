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
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.BESTWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.JSONWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TEST_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TROPHYWORKER_TIME
import com.bigbigdw.manavarasetting.ui.theme.color4186e1
import com.bigbigdw.manavarasetting.ui.theme.colorB3000000
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
    val TROPHYWORKER_TIME =
        dataStore.getDataStoreString(TROPHYWORKER_TIME).collectAsState(initial = "")

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

        ItemMainSetting(
            image = R.drawable.icon_setting_wht,
            titleWorker = "테스트 WORKER : ",
            valueWorker = TEST_TIME.value ?: "알 수 없음",
        )

        ItemMainSetting(
            image = R.drawable.icon_best_wht,
            titleWorker = "베스트 WORKER : ",
            valueWorker = BESTWORKER_TIME.value ?: "알 수 없음",
        )

        ItemMainSetting(
            image = R.drawable.icon_json_wht,
            titleWorker = "JSON WORKER : ",
            valueWorker = JSONWORKER_TIME.value ?: "알 수 없음",
        )

        ItemMainSetting(
            image = R.drawable.icon_trophy_wht,
            titleWorker = "트로피 WORKER : ",
            valueWorker = TROPHYWORKER_TIME.value ?: "알 수 없음",
        )
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

        val mRootRef = FirebaseDatabase.getInstance().reference.child("WORKER")

        mRootRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    val dataStore = DataStoreManager(context)

                    val TESTTIME: String? =
                        dataSnapshot.child("WORKER_TEST").getValue(String::class.java)
                    val BESTWORKERTIME: String? =
                        dataSnapshot.child("WORKER_BEST").getValue(String::class.java)
                    val JSONWORKERTIME: String? =
                        dataSnapshot.child("WORKER_JSON").getValue(String::class.java)
                    val TROPHYWORKERTIME: String? =
                        dataSnapshot.child("WORKER_TROPHY").getValue(String::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        dataStore.setDataStoreString(TEST_TIME, TESTTIME ?: "")
                        dataStore.setDataStoreString(BESTWORKER_TIME, BESTWORKERTIME ?: "")
                        dataStore.setDataStoreString(JSONWORKER_TIME, JSONWORKERTIME ?: "")
                        dataStore.setDataStoreString(TROPHYWORKER_TIME, TROPHYWORKERTIME ?: "")
                        ManavaraSettingWidget.update(context, glanceId)
                    }

                } else {
                    Log.d("HIHI", "FALSE")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
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