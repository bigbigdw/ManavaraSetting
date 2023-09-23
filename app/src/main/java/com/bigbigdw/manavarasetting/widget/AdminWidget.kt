package com.bigbigdw.manavarasetting.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.layout.size
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.BESTWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.JSONWORKER_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TESTKEY
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TEST_TIME
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager.Companion.TROPHYWORKER_TIME
import com.bigbigdw.manavarasetting.util.FCM.postFCMAlertTest
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerInterval
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerStatus
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTag
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTimeMill
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.worker
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

    val worker = currentState(key = ManavaraSettingWidget.worker) ?: "활성화 되지 않음"
    val test = dataStore.getDataStoreString(TESTKEY).collectAsState(initial = "")

    val TEST_TIME = dataStore.getDataStoreString(TEST_TIME).collectAsState(initial = "")
    val BESTWORKER_TIME = dataStore.getDataStoreString(BESTWORKER_TIME).collectAsState(initial = "")
    val JSONWORKER_TIME = dataStore.getDataStoreString(JSONWORKER_TIME).collectAsState(initial = "")
    val TROPHYWORKER_TIME = dataStore.getDataStoreString(TROPHYWORKER_TIME).collectAsState(initial = "")

    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Where to?",
            modifier = GlanceModifier.padding(12.dp)
        )
        Spacer(modifier = GlanceModifier.size(12.dp))
        Button(
            modifier = GlanceModifier.background(color = Color.Red),
            text = "postFCMAlertTest",
            onClick = { postFCMAlertTest(context = context, message = "테스트") }
        )
        Spacer(modifier = GlanceModifier.size(12.dp))

        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = GlanceModifier.size(12.dp))
            Button(
                modifier = GlanceModifier.background(color = Color.Red),
                text = "TEST_TIME : ${TEST_TIME.value}",
                onClick = actionRunCallback(
                    callbackClass = WidgetUpdate::class.java,
                )
            )
            Spacer(modifier = GlanceModifier.size(12.dp))
            Button(
                modifier = GlanceModifier.background(color = Color.Red),
                text = "BESTWORKER_TIME : ${BESTWORKER_TIME.value}",
                onClick = actionRunCallback(
                    callbackClass = WidgetUpdate::class.java,
                )
            )
            Spacer(modifier = GlanceModifier.size(12.dp))
            Button(
                modifier = GlanceModifier.background(color = Color.Red),
                text = "JSONWORKER_TIME : ${JSONWORKER_TIME.value}",
                onClick = actionRunCallback(
                    callbackClass = WidgetUpdate::class.java,
                )
            )
            Spacer(modifier = GlanceModifier.size(12.dp))
            Button(
                modifier = GlanceModifier.background(color = Color.Red),
                text = "TROPHYWORKER_TIME : ${TROPHYWORKER_TIME.value}",
                onClick = actionRunCallback(
                    callbackClass = WidgetUpdate::class.java,
                )
            )
        }

    }
}

@Composable
fun test() {
    // create your AppWidget here
    val count = currentState(key = TESTKEY) ?: 0
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                color = ColorProvider(Color.White),
                fontSize = 26.sp
            )
        )
        Button(
            text = "Inc",
            onClick = actionRunCallback(IncrementActionCallback::class.java)
        )
    }
}

@Composable
fun test2(){
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            modifier = GlanceModifier.background(color = Color.Red),
            text = "doWorker",
            onClick = actionRunCallback(
                callbackClass = WidgetCallback::class.java,
                parameters = actionParametersOf(
                    paramWorkerStatus to "DO",
                    paramWorkerTag to "TEST",
                    paramWorkerInterval to 30,
                    paramWorkerTimeMill to TimeUnit.MINUTES
                )
            )
        )
        Spacer(modifier = GlanceModifier.size(12.dp))
        Button(
            modifier = GlanceModifier.background(color = Color.Red),
            text = "cancelWorker",
            onClick = actionRunCallback(
                callbackClass = WidgetCallback::class.java,
                parameters = actionParametersOf(
                    paramWorkerStatus to "CANCEL",
                    paramWorkerTag to "TEST"
                )
            )
        )

        Spacer(modifier = GlanceModifier.size(12.dp))
        Button(
            modifier = GlanceModifier.background(color = Color.Red),
            text = "TEST",
            onClick = actionRunCallback(
                callbackClass = WidgetCallback::class.java,
                parameters = actionParametersOf(
                    paramWorkerStatus to "CANCEL",
                    paramWorkerTag to "TEST"
                )
            )
        )
        Spacer(modifier = GlanceModifier.size(12.dp))
        Button(
            modifier = GlanceModifier.background(color = Color.Red),
            text = "checkWorker : $worker",
            onClick = actionRunCallback(
                callbackClass = WidgetCallback::class.java,
                parameters = actionParametersOf(paramWorkerStatus to "HAHA")
            )
        )
    }
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
                if(dataSnapshot.exists()){

                    val dataStore = DataStoreManager(context)

                    val TESTTIME: String? = dataSnapshot.child("TEST_TIME").getValue(String::class.java)
                    val BESTWORKERTIME: String? = dataSnapshot.child("BESTWORKER_TIME").getValue(String::class.java)
                    val JSONWORKERTIME: String? = dataSnapshot.child("JSONWORKER_TIME").getValue(String::class.java)
                    val TROPHYWORKERTIME: String? = dataSnapshot.child("TROPHYWORKER_TIME").getValue(String::class.java)

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

class IncrementActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[ManavaraSettingWidget.countKey]

            if (currentCount != null) {
                prefs[ManavaraSettingWidget.countKey] = currentCount + 1
            } else {
                prefs[ManavaraSettingWidget.countKey] = 1
            }
        }
        ManavaraSettingWidget.update(context, glanceId)
    }
}