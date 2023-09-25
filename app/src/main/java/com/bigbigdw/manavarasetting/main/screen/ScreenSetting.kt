package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color20459e
import com.bigbigdw.manavarasetting.ui.theme.color21c2ec
import com.bigbigdw.manavarasetting.ui.theme.color31c3ae
import com.bigbigdw.manavarasetting.ui.theme.color4ad7cf
import com.bigbigdw.manavarasetting.ui.theme.color52a9ff
import com.bigbigdw.manavarasetting.ui.theme.color5372de
import com.bigbigdw.manavarasetting.ui.theme.color64c157
import com.bigbigdw.manavarasetting.ui.theme.color7c81ff
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.color998df9
import com.bigbigdw.manavarasetting.ui.theme.colorabd436
import com.bigbigdw.manavarasetting.ui.theme.colorea927c
import com.bigbigdw.manavarasetting.ui.theme.colorf17fa0
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6

@Composable
fun ScreenMainSetting(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
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

                Spacer(modifier = Modifier.width(16.dp).fillMaxHeight().background(color = colorf6f6f6))

                ScreenTablet(title = getMenu)

            } else {
                ScreenSettingMobileContents(viewModelMain = viewModelMain)
            }
        }
    }
}

@Composable
fun ScreenSettingMobileContents(viewModelMain: ViewModelMain){

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ItemMainSetting(
            image = R.drawable.icon_setting_gr,
            titleWorker = "테스트 WORKER : ",
            valueWorker = viewModelMain.state.collectAsState().value.statusTest,
            statusTitle = "테스트 갱신시간 : ",
            valueStatus = viewModelMain.state.collectAsState().value.timeTest
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

        Spacer(modifier = Modifier.size(24.dp))

        BtnMobile(func = { viewModelMain.getDataStoreStatus(context = context, workManager = workManager) }, btnText = "WORKER 최신화")

        BtnMobile(func = { viewModelMain.getDataStoreFCMCount(context = context) }, btnText = "FCM COUNT 최신화")

        Spacer(modifier = Modifier.size(120.dp))
    }
}

@Composable
fun ScreenSettingTabletContents(setMenu: (String) -> Unit, getMenu: String){

    Column(
        modifier = Modifier
            .width(334.dp)
            .fillMaxHeight()
            .background(color = colorf6f6f6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "세팅바라",
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(8.dp))

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_setting_wht,
            title = "세팅바라 현황",
            body = "Periodic Worker 현황",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4ad7cf,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 토큰 생성",
            body = "기기별 FCM 토큰 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372de,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 공지사항",
            body = "FCM으로 공지사항 등록",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color998df9,
            image = R.drawable.icon_fcm_wht,
            title = "FCM Worker",
            body = "FCM 자동화 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = colorea927c,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 현황",
            body = "FCM 푸시 현황 확인",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorabd436,
            image = R.drawable.icon_best_wht,
            title = "베스트 리스트 갱신",
            body = "베스트 리스트 수동 갱신",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = colorf17fa0,
            image = R.drawable.icon_best_wht,
            title = "베스트 Worker",
            body = "베스트 Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color21c2ec,
            image = R.drawable.icon_json_wht,
            title = "DAY JSON 확인",
            body = "오늘의 베스트 리스트 JSON 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color31c3ae,
            image = R.drawable.icon_json_wht,
            title = "WEEK JSON 확인",
            body = "이번주 베스트 리스트 JSON 생성",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color7c81ff,
            image = R.drawable.icon_json_wht,
            title = "WEEK JSON 업데이트",
            body = "이번주 베스트 리스트 갱신",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color64c157,
            image = R.drawable.icon_json_wht,
            title = "JSON Worker",
            body = "JSON Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

        tabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 정산",
            body = "오늘의 트로피를 수동 정산",
            setMenu = setMenu,
            getMenu = getMenu
        )

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 Worker",
            body = "트로피 Worker 제어",
            setMenu = setMenu,
            getMenu = getMenu
        )

    }
}

@Composable
fun tabletBorderLine(){
    Spacer(modifier = Modifier.size(8.dp))
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = color8e8e8e))
    Spacer(modifier = Modifier.size(8.dp))
}