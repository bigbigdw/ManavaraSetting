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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color898989
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenreKor
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageMonth
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.google.gson.JsonArray
import java.util.concurrent.TimeUnit

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
                .background(color = colorf6f6f6)
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
                    ContentsFCMManage(lineTest = lineTest)
                }
                "FCM 공지사항 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "NOTICE")
                }
                "FCM 알림 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "ALERT")
                }
                "베스트 리스트 관리" -> {
                    ContentsBestManage(lineBest = lineBest)
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
                    ContentsJsonManage(lineJson = lineJson)
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
                "JSON 월간 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType,
                        type = "월간"
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
                "JSON 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "JSON")
                }
                "트로피 정산 관리" -> {
                    ContentsTrophyManage(lineTrophy = lineTrophy)
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
        color = color8e8e8e,
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
        color = color8e8e8e,
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
        color = color8e8e8e,
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
        color = color8e8e8e,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentsFCMManage(lineTest: List<MainSettingLine>) {

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val workManager = WorkManager.getInstance(context)
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    val itemFcmWorker = listOf(
        MainSettingLine(title = "테스트 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "TEST",
                timeMill = TimeUnit.MINUTES
            )
        }),
        MainSettingLine(title = "테스트 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "TEST"
            )
        }),
        MainSettingLine(title = "테스트 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "TEST"
            )
        }),
    )

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = { FCM.getFCMToken(context = context) },
        modifier = Modifier
            .padding(8.dp, 0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        content = {

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FCM 토큰",
                        color = color000000,
                        fontSize = 18.sp,
                    )
                }

                Spacer(modifier = Modifier.size(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dataStore.getDataStoreString(DataStoreManager.FCM_TOKEN).collectAsState(initial = "").value ?: "",
                        color = color8e8e8e,
                        fontSize = 16.sp,
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    )

    Spacer(modifier = Modifier.size(32.dp))

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        onClick = {
            FCM.postFCMAlertTest(context = context, message = "테스트")
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
                    text = "푸시 알림 테스트",
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
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineTest.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineTest.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "FCM 매니저",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemFcmWorker.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemFcmWorker.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "FCM 공지사항 등록",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            TextField(
                value = getFCM.title,
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { Text("푸시 알림 제목", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(16.dp))

            TextField(
                value = getFCM.body,
                onValueChange = {
                    setFCM(getFCM.copy(body = it))
                },
                label = { Text("푸시 알림 내용", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                BtnMobile(
                    func = { FCM.postFCMAlert(context = context, getFCM = getFCM) },
                    btnText = "공지사항 등록"
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
fun ContentsBestManage(lineBest: List<MainSettingLine>) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val itemBestWorker = listOf(
        MainSettingLine(title = "베스트 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 3,
                tag = "BEST",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "베스트 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(workManager = workManager,  tag = "BEST")
        }),
        MainSettingLine(title = "베스트 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "BEST"
            )
        }),
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemBestWorker.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemBestWorker.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "베스트 현황",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineBest.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineBest.size - 1 == index
                )
            }
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
        color = color8e8e8e,
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
        itemList.add(MainSettingLine(title = "네이버 시리즈 $type JSON 리스트 ${getNaverSeriesGenreKor(j)}", value = getNaverSeriesGenre(j)))
    }

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "네이버 시리즈",
        fontSize = 16.sp,
        color = color8e8e8e,
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
fun ContentsJsonManage(lineJson: List<MainSettingLine>) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val itemJsonWorker = listOf(
        MainSettingLine(title = "JSON WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 6,
                tag = "JSON",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON"
            )
        }),
        MainSettingLine(title = "JSON WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "JSON"
            )
        }),
    )

    val lineUpdateSelf = listOf(
        MainSettingLine(title = "JSON DAY 생성", onClick = {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageDay(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )
            }
            FCM.postFCMAlertTest(context = context, message = "JSON 최신화가 완료되었습니다")
        }),
        MainSettingLine(title = "JSON WEEK 생성", onClick = {
            for (j in NaverSeriesGenre) {

                val jsonArray = JsonArray()

                for (i in 0..6) {
                    jsonArray.add("")
                }

                makeWeekJson(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    jsonArray = jsonArray
                )
            }
        }),
        MainSettingLine(title = "JSON WEEK 업데이트", onClick = {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageWeek(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )
            }
        }),
        MainSettingLine(title = "JSON MONTH 업데이트", onClick = {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageMonth(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j)
                )
            }
        })

    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorker.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorker.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 수동 업데이트",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineUpdateSelf.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineUpdateSelf.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "베스트 현황",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineJson.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineJson.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsTrophyManage(lineTrophy: List<MainSettingLine>) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val itemJsonWorker = listOf(
        MainSettingLine(title = "TROPHY WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 9,
                tag = "TROPHY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "TROPHY WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "TROPHY"
            )
        }),
        MainSettingLine(title = "TROPHY WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "TROPHY"
            )
        }),
    )

    val lineUpdateSelf = listOf(
        MainSettingLine(title = "트로피 정산", onClick = {
            for (j in NaverSeriesGenre) {
                calculateTrophy(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
            }
            FCM.postFCMAlertTest(context = context, message = "트로피 정산이 완료되었습니다")
        })
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorker.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorker.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 수동 업데이트",
        fontSize = 16.sp,
        color = color8e8e8e,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            lineUpdateSelf.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    value = item.value,
                    isLast = lineUpdateSelf.size - 1 == index
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "트로피 정산 현황",
        fontSize = 16.sp,
        color = color8e8e8e,
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