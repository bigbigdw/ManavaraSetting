package com.bigbigdw.manavarasetting.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.layout.Spacer
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.util.FCM.postFCMAlertTest
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import java.util.concurrent.TimeUnit

class AdminWidget: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget
}

object CounterWidget: GlanceAppWidget() {

    val countKey = intPreferencesKey("count")

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {

            val workManager = WorkManager.getInstance(context)

            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
                Spacer(modifier = GlanceModifier.padding(0.dp, 12.dp, 0.dp, 0.dp))
                Button(
                    text = "postFCMAlertTest",
                    onClick = { postFCMAlertTest(context = context) }
                )
                Spacer(modifier = GlanceModifier.padding(0.dp, 12.dp, 0.dp, 0.dp))
                Button(
                    text = "doWorker",
                    onClick = { PeriodicWorker.doWorker(workManager = workManager, context = context, tag = "TEST", repeatInterval = 15, timeMill = TimeUnit.MINUTES) }
                )
                Spacer(modifier = GlanceModifier.padding(0.dp, 12.dp, 0.dp, 0.dp))
                Button(
                    text = "cancelWorker",
                    onClick = { PeriodicWorker.cancelWorker(workManager = workManager, context = context, tag = "TEST") }
                )
                Spacer(modifier = GlanceModifier.padding(0.dp, 12.dp, 0.dp, 0.dp))
                Button(
                    text = "checkWorker",
                    onClick = { PeriodicWorker.checkWorker(workManager = workManager, context = context, tag = "TEST") }
                )
            }
        }
    }
}

@Composable
fun test(){
    // create your AppWidget here
    val count = currentState(key = CounterWidget.countKey) ?: 0
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

class IncrementActionCallback: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[CounterWidget.countKey]
            if(currentCount != null) {
                prefs[CounterWidget.countKey] = currentCount + 1
            } else {
                prefs[CounterWidget.countKey] = 1
            }
        }
        CounterWidget.update(context, glanceId)
    }
}