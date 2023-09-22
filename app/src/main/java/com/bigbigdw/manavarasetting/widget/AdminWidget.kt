package com.bigbigdw.manavarasetting.widget

import android.content.Context
import androidx.compose.runtime.Composable
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
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.util.FCM.postFCMAlertTest
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerInterval
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerStatus
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTag
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTimeMill
import kotlinx.coroutines.Dispatchers
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

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            val worker = currentState(key = ManavaraSettingWidget.worker) ?: "활성화 되지 않음"

            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))

                Spacer(modifier = GlanceModifier.padding(0.dp, 0.dp, 0.dp, 12.dp))

                Button(
                    modifier = GlanceModifier.background(color = Color.Red),
                    text = "postFCMAlertTest",
                    onClick = { postFCMAlertTest(context = context) }
                )
                Spacer(modifier = GlanceModifier.padding(0.dp, 0.dp, 0.dp, 12.dp))
                Button(
                    modifier = GlanceModifier.background(color = Color.Red),
                    text = "doWorker",
                    onClick = actionRunCallback(
                        callbackClass = WidgetCallback::class.java,
                        parameters = actionParametersOf(paramWorkerStatus to "DO", paramWorkerTag to "TEST", paramWorkerInterval to 30, paramWorkerTimeMill to TimeUnit.MINUTES)
                    )
                )
                Spacer(modifier = GlanceModifier.padding(0.dp, 0.dp, 0.dp, 12.dp))
                Button(
                    modifier = GlanceModifier.background(color = Color.Red),
                    text = "cancelWorker",
                    onClick = actionRunCallback(
                        callbackClass = WidgetCallback::class.java,
                        parameters = actionParametersOf(paramWorkerStatus to "CANCEL", paramWorkerTag to "TEST")
                    )
                )

                Spacer(modifier = GlanceModifier.padding(0.dp, 0.dp, 0.dp, 12.dp))
                Button(
                    modifier = GlanceModifier.background(color = Color.Red),
                    text = "TEST",
                    onClick = actionRunCallback(
                        callbackClass = WidgetCallback::class.java,
                        parameters = actionParametersOf(paramWorkerStatus to "CANCEL", paramWorkerTag to "TEST")
                    )
                )


                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = GlanceModifier.padding(0.dp, 0.dp, 0.dp, 12.dp))
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
        }
    }
}

@Composable
fun test() {
    // create your AppWidget here
    val count = currentState(key = ManavaraSettingWidget.countKey) ?: 0
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

            if(parameters[paramWorkerTag] != null){
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