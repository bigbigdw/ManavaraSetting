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
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
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
                repeatInterval = 4,
                tag = "JSON_TODAY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 투데이 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_TODAY"
            )
        })
    )

    val itemJsonWorkerWeek = listOf(
        MainSettingLine(title = "JSON 주간 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 5,
                tag = "JSON_WEEK",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 주간 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_WEEK"
            )
        })
    )

    val itemJsonWorkerMonth = listOf(
        MainSettingLine(title = "JSON 월간 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 6,
                tag = "JSON_MONTH",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 월간 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_MONTH"
            )
        })
    )

    val itemJsonWorkerWeekTrophy = listOf(
        MainSettingLine(title = "JSON 주간 트로피 WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 8,
                tag = "JSON_WEEK_TROPHY",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "JSON 주간 트로피 WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "JSON_WEEK_TROPHY"
            )
        })
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
        })
    )

    val lineUpdateSelf = listOf(
        MainSettingLine(title = "JSON DAY 생성", onClick = {
            for (j in NaverSeriesComicGenre) {
                uploadJsonArrayToStorageDay(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "COMIC"
                )
            }
            FCM.postFCMAlertTest(context = context, message = "JSON 최신화가 완료되었습니다")
        }),
        MainSettingLine(title = "JSON WEEK 생성", onClick = {
            for (j in NaverSeriesComicGenre) {

                val jsonArray = JsonArray()

                for (i in 0..6) {
                    jsonArray.add("")
                }

                makeWeekJson(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    jsonArray = jsonArray,
                    type = "COMIC"
                )
            }
        }),
        MainSettingLine(title = "JSON WEEK 업데이트", onClick = {
            for (j in NaverSeriesComicGenre) {
                uploadJsonArrayToStorageWeek(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "COMIC"
                )
            }
        }),
        MainSettingLine(title = "JSON MONTH 업데이트", onClick = {
            for (j in NaverSeriesComicGenre) {
                uploadJsonArrayToStorageMonth(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    type = "COMIC"
                )
            }
        }),
        MainSettingLine(title = "JSON WEEK 트로피 업데이트", onClick = {
            for (j in NaverSeriesComicGenre) {
                uploadJsonArrayToStorageTrophy(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    menu = "주간",
                    type = "COMIC"
                )
            }
        }),
        MainSettingLine(title = "JSON MONTH 트로피 업데이트", onClick = {
            for (j in NaverSeriesComicGenre) {
                uploadJsonArrayToStorageTrophy(
                    platform = "NAVER_SERIES",
                    genre = getNaverSeriesGenre(j),
                    menu = "월간",
                    type = "COMIC"
                )
            }
        }),
    )

    TabletContentWrap {
        itemJsonWorkerToday.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemJsonWorkerToday.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 주간")

    TabletContentWrap {
        itemJsonWorkerWeek.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemJsonWorkerWeek.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 월간")

    TabletContentWrap {
        itemJsonWorkerMonth.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemJsonWorkerMonth.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 주간 트로피")

    TabletContentWrap {
        itemJsonWorkerWeekTrophy.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemJsonWorkerWeekTrophy.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 월간 트로피")

    TabletContentWrap {
        itemJsonWorkerMonthTrophy.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemJsonWorkerMonthTrophy.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 수동 업데이트")

    TabletContentWrap {
        lineUpdateSelf.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineUpdateSelf.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "JSON 현황")

    TabletContentWrap {
        lineJson.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineJson.size - 1 == index
            )
        }
    }

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

    for (j in NaverSeriesComicGenre) {
        itemList.add(MainSettingLine(title = "$type JSON ${getNaverSeriesGenreKor(j)}", value = getNaverSeriesGenre(j)))
    }

    Text(
        modifier = Modifier.padding(32.dp, 8.dp),
        text = "네이버 시리즈",
        fontSize = 16.sp,
        color = color8E8E8E,
        fontWeight = FontWeight(weight = 700)
    )

    TabletContentWrap {
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

    Spacer(modifier = Modifier.size(60.dp))
}