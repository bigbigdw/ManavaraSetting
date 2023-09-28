package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenreKor

@Composable
fun ScreenTablet(
    title: String,
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
    viewModelMain: ViewModelMain,
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPageType: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        setDetailPage(false)
                    },
                text = title,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            Spacer(modifier = Modifier.size(16.dp))

            when (title) {
                "세팅바라 현황" -> {
                    ContentsSetting(
                        lineTest = lineTest,
                        lineBest = lineBest,
                        lineJson = lineJson,
                        lineTrophy = lineTrophy,
                        viewModelMain = viewModelMain
                    )
                }
                "FCM 관리" -> {
                    ContentsFCM(lineTest = lineTest)
                }
                "FCM 공지사항 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "NOTICE")
                }
                "FCM 알림 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "ALERT")
                }
                "베스트 리스트 관리" -> {
                    ContentsBest(lineBest = lineBest)
                }
                "베스트 BOOK 리스트" -> {
                    ContentsBestList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType
                    )
                }
                "베스트 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "BEST")
                }
                "JSON 베스트 관리" -> {
                    ContentsJson(lineJson = lineJson)
                }
                "JSON 투데이 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "투데이"
                    )
                }
                "JSON 주간 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "주간"
                    )
                }
                "JSON 주간 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "주간"
                    )
                }
                "JSON 월간 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "월간"
                    )
                }
                "JSON 주간 누적 트로피 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "주간 트로피"
                    )
                }
                "JSON 월간 누적 트로피 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "월간 트로피"
                    )
                }
                "JSON 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "JSON")
                }
                "트로피 정산 관리" -> {
                    ContentsTrophy(lineTrophy = lineTrophy)
                }
                "트로피 주간 토탈 리스트" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "트로피 주간"
                    )
                }
                "트로피 월간 토탈 리스트" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "트로피 월간"
                    )
                }
                "트로피 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "JSON")
                }
            }
        }
    }
}

@Composable
fun ContentsSetting(
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
    viewModelMain : ViewModelMain
) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { viewModelMain.getDataStoreStatus(context = context, workManager = workManager) },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKER 최신화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { viewModelMain.getDataStoreFCMCount(context = context) },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FCM 카운트 최신화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { PeriodicWorker.cancelAllWorker(
            workManager = workManager,
        ) },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "모든 Worker 취소",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "테스트 현황",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineTest.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineTrophy.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "베스트 현황",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineBest.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineTrophy.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 현황",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineJson.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineTrophy.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "트로피 현황",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineTrophy.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineTrophy.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsFCMList(viewModelMain: ViewModelMain, child : String){


    val fcmAlertList = when (child) {
        "ALERT" -> {
            viewModelMain.getFCMList(child = child)
            viewModelMain.state.collectAsState().value.fcmAlertList
        }
        "NOTICE" -> {
            viewModelMain.getFCMList(child = child)
            viewModelMain.getFCMList(child = child)
            viewModelMain.state.collectAsState().value.fcmNoticeList
        }
        "BEST" -> {
            viewModelMain.state.collectAsState().value.fcmBestList
        }
        "JSON" -> {
            viewModelMain.state.collectAsState().value.fcmJsonList
        }
        "TROPHY" -> {
            viewModelMain.state.collectAsState().value.fcmTrophyList
        }
        else -> {
            viewModelMain.state.collectAsState().value.fcmAlertList
        }
    }

    TabletContentWrap(
        radius = 5,
        content = {
            Spacer(modifier = Modifier.size(8.dp))

            fcmAlertList.forEachIndexed { index, item ->
                ItemTabletFCMList(
                    item = item,
                    isLast = fcmAlertList.size - 1 == index
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestList(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPageType: (String) -> Unit,
) {

    val context = LocalContext.current
    val itemList = ArrayList<MainSettingLine>()

    for (j in NaverSeriesGenre) {
        itemList.add(MainSettingLine(title = "네이버 시리즈 베스트 리스트 ${getNaverSeriesGenreKor(j)}", value = getNaverSeriesGenre(j)))
    }

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = {
            for (j in NaverSeriesGenre) {

                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_WEEK")
                        .removeValue()
                }

                if (DBDate.datedd() == "01") {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_MONTH")
                        .removeValue()
                }

                for (i in 1..5) {
                    Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                }
            }
            FCM.postFCMAlertTest(context = context, message = "베스트 리스트가 갱신되었습니다")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(50.dp),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "베스트 리스트 수동 갱신",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "네이버 시리즈",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 5,
        content = {
            itemList.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemList.size - 1 == index,
                    onClick = {
                        setDetailPage(true)
                        setDetailMenu(item.title)
                        setDetailPageType(item.value)
                    }
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestJsonList(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPageType: (String) -> Unit,
    type : String,
) {

    val itemList = ArrayList<MainSettingLine>()

    for (j in NaverSeriesGenre) {
        itemList.add(MainSettingLine(title = "$type JSON ${getNaverSeriesGenreKor(j)}", value = getNaverSeriesGenre(j)))
    }

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "네이버 시리즈",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 5,
        content = {
            itemList.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemList.size - 1 == index,
                    onClick = {
                        setDetailPage(true)
                        setDetailMenu(item.title)
                        setDetailPageType(item.value)
                    }
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

