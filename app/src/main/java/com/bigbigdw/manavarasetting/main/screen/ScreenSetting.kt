package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6

@Composable
fun ScreenMainSetting(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    viewModelMain.getDataStoreStatus(context = context, workManager = workManager)
    viewModelMain.getDataStoreFCMCount(context = context)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if(isExpandedScreen){

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                ScreenSettingTabletContents(setMenu = setMenu, getMenu = getMenu)

                Spacer(modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = colorf6f6f6))

                ScreenTablet(title = getMenu)

            } else {
                ScreenSettingMobileContents(
                    viewModelMain = viewModelMain,
                    lineTest = lineTest,
                    lineBest = lineBest,
                    lineJson = lineJson,
                    lineTrophy = lineTrophy
                )
            }
        }
    }
}

@Composable
fun ScreenSettingMobileContents(
    viewModelMain: ViewModelMain,
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorFFFFFF)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        lineTest.forEach { item ->
            ItemMainSettingSingle(
                image = R.drawable.icon_setting_gr,
                titleWorker = "테스트 ${item.title}",
                valueWorker = item.value,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        lineBest.forEach { item ->
            ItemMainSettingSingle(
                image = R.drawable.icon_best_gr,
                titleWorker = "베스트 ${item.title}",
                valueWorker = item.value,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        lineJson.forEach { item ->
            ItemMainSettingSingle(
                image = R.drawable.icon_json_gr,
                titleWorker = "JSON ${item.title}",
                valueWorker = item.value,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        lineTrophy.forEach { item ->
            ItemMainSettingSingle(
                image = R.drawable.icon_json_gr,
                titleWorker = "트로피 ${item.title}",
                valueWorker = item.value,
            )
        }

        Spacer(modifier = Modifier.size(24.dp))

        BtnMobile(func = { viewModelMain.getDataStoreStatus(context = context, workManager = workManager) }, btnText = "WORKER 최신화")

        BtnMobile(func = { viewModelMain.getDataStoreFCMCount(context = context) }, btnText = "FCM COUNT 최신화")

        Spacer(modifier = Modifier.size(120.dp))
    }
}