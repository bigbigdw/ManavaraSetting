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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.PeriodicWorker

@Composable
fun ScreenTablet(
    title: String,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
    viewModelMain: ViewModelMain,
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailGenre: (String) -> Unit,
    setDetailType: (String) -> Unit,
    lineCount: List<MainSettingLine>
) {

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 16.dp, 0.dp)
                .background(color = colorF6F6F6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
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
                        lineBest = lineBest,
                        lineJson = lineJson,
                        lineTrophy = lineTrophy,
                        viewModelMain = viewModelMain,
                        lineCount = lineCount
                    )
                }
                "FCM 관리" -> {
                    ContentsFCM()
                }
                "FCM 공지사항 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "NOTICE")
                }
                "FCM 알림 리스트" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "ALERT")
                }
                "베스트 리스트 관리" -> {
                    ContentsBest(lineBest = lineBest, viewModelMain = viewModelMain)
                }
                "베스트 BOOK 리스트" -> {
                    ContentsBestList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "베스트 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "BEST")
                }
                "JSON 관리" -> {
                    ContentsJson(viewModelMain = viewModelMain, lineJson = lineJson)
                }
                "JSON 투데이 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 투데이 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "JSON 주간 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "JSON 월간 베스트 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "JSON 주간 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "주간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }

                "JSON 주간 트로피 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "JSON 월간 트로피 현황" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "JSON 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "JSON")
                }
                "트로피 정산 관리" -> {
                    ContentsTrophy(viewModelMain = viewModelMain,lineTrophy = lineTrophy)
                }
                "트로피 주간 토탈 리스트" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 주간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "트로피 월간 토탈 리스트" -> {
                    ContentsBestJsonList(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 월간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                    )
                }
                "트로피 최신화 현황" -> {
                    ContentsFCMList(viewModelMain = viewModelMain, child = "TROPHY")
                }
                "위험 옵션" -> {
                    ContentsDangerOption(viewModelMain = viewModelMain)
                }
                "실험실" -> {
                    ContentsDangerLabs()
                }
            }
        }
    }
}

@Composable
fun ContentsSetting(
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
    viewModelMain: ViewModelMain,
    lineCount: List<MainSettingLine>
) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    TabletContentWrapBtn(
        onClick = { viewModelMain.getDataStoreStatus(context = context) },
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

    TabletContentWrapBtn(
        onClick = { viewModelMain.getDataStoreFCMCount() },
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

    TabletContentWrapBtn(
        onClick = { PeriodicWorker.cancelAllWorker(
            workManager = workManager,
        ) },
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

    ItemTabletTitle(str = "베스트 현황")

    TabletContentWrap {
        lineBest.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineTrophy.size - 1 == index
            )
        }
    }

    ItemTabletTitle(str = "JSON 현황")

    TabletContentWrap {
        lineJson.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineTrophy.size - 1 == index
            )
        }
    }

    ItemTabletTitle(str =  "트로피 현황")

    TabletContentWrap {
        lineTrophy.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineTrophy.size - 1 == index
            )
        }
    }

    ItemTabletTitle(str =  "최신화 횟수 현황")

    TabletContentWrap {
        lineCount.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineCount.size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}