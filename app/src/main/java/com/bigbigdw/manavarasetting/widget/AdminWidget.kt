package com.bigbigdw.manavarasetting.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

import com.bigbigdw.manavarasetting.ui.theme.color1CE3EE
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.ui.theme.colorB3000000
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getDataStoreStatus
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerInterval
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerStatus
import com.bigbigdw.manavarasetting.widget.ManavaraSettingWidget.paramWorkerTag
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

    val MINING_NAVER_SERIES_COMIC = dataStore.getDataStoreString(DataStoreManager.MINING_NAVER_SERIES_COMIC).collectAsState(initial = "")
    val MINING_NAVER_SERIES_NOVEL = dataStore.getDataStoreString(DataStoreManager.MINING_NAVER_SERIES_NOVEL).collectAsState(initial = "")

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = colorB3000000)
            .padding(16.dp)
            .clickable(actionRunCallback(callbackClass = WidgetUpdate::class.java)),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        Spacer(modifier = GlanceModifier.size(4.dp))

        Row(
            modifier = GlanceModifier.background(color = colorB3000000).padding(4.dp),
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
                    color = ColorProvider(colorFFFFFF),
                    fontSize = 24.sp,
                )
            )
        }

        Column(modifier = GlanceModifier.padding(8.dp)){

            Spacer(modifier = GlanceModifier.size(4.dp))

            Text(
                text = "네이버 시리즈 웹툰",
                style = TextStyle(
                    color = ColorProvider(colorFFFFFF),
                    fontSize = 20.sp,
                )
            )

            Spacer(modifier = GlanceModifier.size(4.dp))

        }

        Column(modifier = GlanceModifier.padding(8.dp)){

            Spacer(modifier = GlanceModifier.size(4.dp))

            Text(
                text = "네이버 시리즈 웹소설",
                style = TextStyle(
                    color = ColorProvider(colorFFFFFF),
                    fontSize = 20.sp,
                )
            )

            Spacer(modifier = GlanceModifier.size(4.dp))



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
                color = ColorProvider(colorFFFFFF),
                fontSize = 18.sp,
            )
        )

        Spacer(modifier = GlanceModifier.size(4.dp))

        Text(
            text = valueWorker,
            style = TextStyle(
                color = ColorProvider(color1CE3EE),
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

        getDataStoreStatus(context = context, update = {
            CoroutineScope(Dispatchers.IO).launch {
                ManavaraSettingWidget.update(context, glanceId)
            }
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
                    delayMills = parameters[paramWorkerInterval] ?: 0,
                    tag = parameters[paramWorkerTag] ?: "",
                    platform = "NAVER_SERIES",
                    type = "COMIC"
                )

            } else if (parameters[paramWorkerStatus] == "CANCEL") {
                PeriodicWorker.cancelWorker(
                    workManager = workManager,
                    tag = "TEST",
                    platform = "NAVER_SERIES",
                    type = "COMIC"
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