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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.colorf7f7f7

@Composable
fun ScreenMainSetting(
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    val context = LocalContext.current

    viewModelMain.getDataStoreStatus(context = context, workManager = workManager)
    viewModelMain.getDataStoreFCMCount(context = context)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if(isExpandedScreen){
                ScreenSettingTabletContents(isExpandedScreen = isExpandedScreen, viewModelMain = viewModelMain)
                ScreenTest()
            } else {
                ScreenSettingMobileContents(isExpandedScreen = isExpandedScreen, viewModelMain = viewModelMain)
            }
        }
    }
}

@Composable
fun ScreenSettingMobileContents(isExpandedScreen : Boolean, viewModelMain : ViewModelMain){

    val modifier = if (isExpandedScreen) {
        Modifier
            .width(334.dp)
            .fillMaxHeight()
            .background(color = colorf7f7f7)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    } else {
        Modifier
            .fillMaxSize()
            .background(color = colorf7f7f7)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ItemMainSetting(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusTest,
            statusTitle = "테스트 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.timeTest,
        )

        ItemMainSetting(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countTest,
            statusTitle = "테스트 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayTest,
        )

        ItemMainSettingSingle(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillTest,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusBest,
            statusTitle = "베스트 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testBest,
        )
        ItemMainSetting(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countBest,
            statusTitle = "베스트 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayBest,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillBest,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusJson,
            statusTitle = "JSON 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testJson,
        )
        ItemMainSetting(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countJson,
            statusTitle = "JSON 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayJson,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillJson,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusTrophy,
            statusTitle = "트로피 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testTrophy,
        )
        ItemMainSetting(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countTrophy,
            statusTitle = "트로피 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayTrophy,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillTrophy,
        )

        Spacer(modifier = Modifier.size(120.dp))
    }
}

@Composable
fun ScreenSettingTabletContents(isExpandedScreen : Boolean, viewModelMain : ViewModelMain){

    val modifier = if (isExpandedScreen) {
        Modifier
            .width(334.dp)
            .fillMaxHeight()
            .background(color = colorf7f7f7)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    } else {
        Modifier
            .fillMaxSize()
            .background(color = colorf7f7f7)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ItemMainSetting(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusTest,
            statusTitle = "테스트 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.timeTest,
        )

        ItemMainSetting(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countTest,
            statusTitle = "테스트 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayTest,
        )

        ItemMainSettingSingle(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillTest,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusBest,
            statusTitle = "베스트 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testBest,
        )
        ItemMainSetting(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countBest,
            statusTitle = "베스트 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayBest,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_best_gr,
            titleWorker = "베스트 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillBest,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusJson,
            statusTitle = "JSON 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testJson,
        )
        ItemMainSetting(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countJson,
            statusTitle = "JSON 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayJson,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_json_gr,
            titleWorker = "JSON 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillJson,
        )

        Spacer(modifier = Modifier.size(24.dp))

        ItemMainSetting(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusTrophy,
            statusTitle = "트로피 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.testTrophy,
        )
        ItemMainSetting(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 호출 횟수 : ",
            valueWorker = viewModelMain.state.collectAsState().value.countTrophy,
            statusTitle = "트로피 금일 호출 횟수 : ",
            valueStatus = viewModelMain.state.collectAsState().value.countTodayTrophy,
        )
        ItemMainSettingSingle(
            image = R.drawable.icon_trophy_gr,
            titleWorker = "트로피 호출 주기 : ",
            valueWorker = viewModelMain.state.collectAsState().value.timeMillTrophy,
        )

        Spacer(modifier = Modifier.size(120.dp))
    }
}