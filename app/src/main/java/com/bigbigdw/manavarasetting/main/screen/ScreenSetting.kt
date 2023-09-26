package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color21c2ec
import com.bigbigdw.manavarasetting.ui.theme.color31c3ae
import com.bigbigdw.manavarasetting.ui.theme.color4996e8
import com.bigbigdw.manavarasetting.ui.theme.color4ad7cf
import com.bigbigdw.manavarasetting.ui.theme.color52a9ff
import com.bigbigdw.manavarasetting.ui.theme.color536fd2
import com.bigbigdw.manavarasetting.ui.theme.color5372de
import com.bigbigdw.manavarasetting.ui.theme.color64c157
import com.bigbigdw.manavarasetting.ui.theme.color79b4f8
import com.bigbigdw.manavarasetting.ui.theme.color7c81ff
import com.bigbigdw.manavarasetting.ui.theme.color80bf78
import com.bigbigdw.manavarasetting.ui.theme.color91cec7
import com.bigbigdw.manavarasetting.ui.theme.color998df9
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.colorabd436
import com.bigbigdw.manavarasetting.ui.theme.colore9e9e9
import com.bigbigdw.manavarasetting.ui.theme.colorea927c
import com.bigbigdw.manavarasetting.ui.theme.colorf17666
import com.bigbigdw.manavarasetting.ui.theme.colorf17fa0
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6
import com.bigbigdw.manavarasetting.ui.theme.colorfdc24e

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

    var isInit by remember { mutableStateOf(false) }

    if(!isInit){
        viewModelMain.getDataStoreStatus(context = context, workManager = workManager)
        viewModelMain.getDataStoreFCMCount(context = context)
        isInit = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if(isExpandedScreen){

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }
                val (getDetailPageType, setDetailPageType) = remember { mutableStateOf("") }
                val (getDetailMenu, setDetailMenu) = remember { mutableStateOf("") }

                ScreenSettingTabletContents(setMenu = setMenu, getMenu = getMenu, onClick = {setDetailPage(false)})

                Spacer(modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = colorf6f6f6))

                if(getDetailPage){
                    ScreenTabletDetail(
                        setDetailPage = setDetailPage,
                        getDetailMenu = getDetailMenu,
                        viewModelMain = viewModelMain,
                        getDetailPageType = getDetailPageType
                    )
                } else {
                    ScreenTablet(
                        title = getMenu,
                        lineTest = lineTest,
                        lineBest = lineBest,
                        lineJson = lineJson,
                        lineTrophy = lineTrophy,
                        viewModelMain = viewModelMain,
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType
                    )
                }

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

        BtnMobile(func = {
            viewModelMain.getDataStoreStatus(
                context = context,
                workManager = workManager
            )
        }, btnText = "WORKER 최신화")

        BtnMobile(func = { viewModelMain.getDataStoreFCMCount(context = context) }, btnText = "FCM COUNT 최신화")

        Spacer(modifier = Modifier.size(120.dp))
    }
}

@Composable
fun ScreenSettingTabletContents(setMenu: (String) -> Unit, getMenu: String, onClick : () -> Unit){

    Column(
        modifier = Modifier
            .width(334.dp)
            .fillMaxHeight()
            .background(color = colorf6f6f6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "세팅바라",
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(16.dp))

        ItemMainSettingSingleTablet(
            containerColor = color52a9ff,
            image = R.drawable.ic_launcher,
            title = "세팅바라 현황",
            body = "Periodic Worker 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4ad7cf,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 관리",
            body = "FCM 테스트 & 공지사항 등록 & 토큰 획득",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372de,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 공지사항 리스트",
            body = "NOTICE 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color998df9,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 알림 리스트",
            body = "ALERT 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorea927c,
            image = R.drawable.icon_best_wht,
            title = "베스트 리스트 관리",
            body = "베스트 리스트 수동 갱신 & Worker",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorabd436,
            image = R.drawable.icon_best_wht,
            title = "베스트 BOOK 리스트",
            body = "장르별 작품 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorf17fa0,
            image = R.drawable.icon_best_wht,
            title = "베스트 최신화 현황",
            body = "시간별 베스트 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color21c2ec,
            image = R.drawable.icon_json_wht,
            title = "베스트 JSON 관리",
            body = "베스트 JSON 수동 갱신 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color31c3ae,
            image = R.drawable.icon_json_wht,
            title = "JSON 투데이 베스트 현황",
            body = "장르별 WEEK 투데이 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color7c81ff,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 베스트 현황",
            body = "WEEK 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color64c157,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 베스트 현황",
            body = "MONTH 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorf17666,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 현황",
            body = "장르별 주간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color536fd2,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 현황",
            body = "장르별 월간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color4996e8,
            image = R.drawable.icon_json_wht,
            title = "JSON 최신화 현황",
            body = "시간별 JSON 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorfdc24e,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 졍산 관리",
            body = "트로피 수동 정산 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color80bf78,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 주간 토탈 리스트",
            body = "장르별 주간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color91cec7,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 월간 토탈 리스트",
            body = "장르별 월간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color79b4f8,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 갱신 현황",
            body = "시간별 트로피 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

//        ItemMainSettingSingleTablet(
//            containerColor = color52a9ff,
//            image = R.drawable.icon_trophy_wht,
//            title = "트로피 월간 토탈 리스트",
//            body = "장르별 월간 트로피 리스트 확인",
//            setMenu = setMenu,
//            getMenu = getMenu,
//            onClick = {onClick()}
//        )

    }
}