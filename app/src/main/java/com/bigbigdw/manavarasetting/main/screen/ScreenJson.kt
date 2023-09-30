package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.ui.theme.color8E8E8E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenreKor
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageMonth
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageTrophy
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.google.gson.JsonArray
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainJson(
    lineJson: List<MainSettingLine>
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
                .padding(16.dp, 0.dp)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            MainHeader(image = R.drawable.icon_json, title = "베스트 JSON 현황")

            ContentsJson(lineJson = lineJson)
        }
    }
}

@Composable
fun ContentsJson(lineJson: List<MainSettingLine>) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val itemJsonWorkerToday = listOf(
        MainSettingLine(title = "JSON 투데이 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 6,
                tag = "JSON_TODAY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 투데이 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_TODAY"
            )
        }),
        MainSettingLine(title = "JSON 투데이 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "JSON_TODAY"
            )
        })
    )

    val itemJsonWorkerWeek = listOf(
        MainSettingLine(title = "JSON 주간 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 7,
                tag = "JSON_WEEK",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 주간 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_WEEK"
            )
        }),
        MainSettingLine(title = "JSON 주간 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "JSON_WEEK"
            )
        }),
    )

    val itemJsonWorkerWeekTrophy = listOf(
        MainSettingLine(title = "JSON 주긴 트로피 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 8,
                tag = "JSON_WEEK_TROPHY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 주긴 트로피 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_WEEK_TROPHY"
            )
        }),
        MainSettingLine(title = "JSON 주긴 트로피 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "JSON_WEEK_TROPHY"
            )
        }),
    )

    val itemJsonWorkerMonthTrophy = listOf(
        MainSettingLine(title = "JSON 월간 트로피 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 9,
                tag = "JSON_MONTH_TROPHY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 월간 트로피 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_MONTH_TROPHY"
            )
        }),
        MainSettingLine(title = "JSON 월간 트로피 WORKER 확인", onClick = {
            PeriodicWorker.checkWorker(
                workManager = workManager,
                tag = "JSON_MONTH_TROPHY"
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
        }),
        MainSettingLine(title = "JSON WEEK 트로피 업데이트", onClick = {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageTrophy(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "주간"
                )
            }
        }),
        MainSettingLine(title = "JSON MONTH 트로피 업데이트", onClick = {
            for (j in NaverSeriesGenre) {
                uploadJsonArrayToStorageTrophy(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "월간"
                )
            }
        }),
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorkerToday.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorkerToday.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 주간",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorkerWeek.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorkerWeek.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 주간 트로피",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorkerWeekTrophy.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorkerWeekTrophy.size - 1 == index,
                    onClick = item.onClick
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(16.dp))

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "JSON 월간 트로피",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap(
        radius = 10,
        content = {
            itemJsonWorkerMonthTrophy.forEachIndexed { index, item ->
                ItemMainTabletContent(
                    title = item.title,
                    isLast = itemJsonWorkerMonthTrophy.size - 1 == index,
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
        color = color8E8E8E,
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
                    isLast = lineJson.size - 1 == index
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