package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.makeWeekJson
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageWeek
import com.google.gson.JsonArray
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainJson(
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf7f7f7)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainHeader(image = R.drawable.icon_json, title = "베스트 JSON 현황")

            ItemMainSetting(
                image = R.drawable.icon_json_gr,
                titleWorker = "WORKER : ",
                valueWorker = viewModelMain.state.collectAsState().value.statusJson,
                statusTitle = "갱신시간 : ",
                valueStatus = viewModelMain.state.collectAsState().value.timeJson,
            )
            ItemMainSetting(
                image = R.drawable.icon_json_gr,
                titleWorker = "호출 횟수 : ",
                valueWorker = viewModelMain.state.collectAsState().value.countJson,
                statusTitle = "금일 호출 횟수 : ",
                valueStatus = viewModelMain.state.collectAsState().value.countTodayJson,
            )
            ItemMainSettingSingle(
                image = R.drawable.icon_json_gr,
                titleWorker = "호출 주기 : ",
                valueWorker = viewModelMain.state.collectAsState().value.timeMillJson,
            )

            BtnMobile(
                func = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageDay(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j)
                        )
                    }
                    FCM.postFCMAlertTest(context = context, message = "DAY JSON 생성이 완료되었습니다")
                },
                btnText = "JSON DAY 생성"
            )

            BtnMobile(
                func = {
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
                },
                btnText = "JSON WEEK 생성"
            )

            BtnMobile(
                func = {
                    for (j in NaverSeriesGenre) {
                        uploadJsonArrayToStorageWeek(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j)
                        )
                    }
                },
                btnText = "JSON WEEK 업데이트"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 6,
                        tag = "BEST_JSON",
                        timeMill = TimeUnit.HOURS
                    )
                },
                btnText = "JSON WORKER 시작"
            )

            BtnMobile(
                func = {
                    for (j in NaverSeriesGenre) {
                        PeriodicWorker.cancelWorker(
                            workManager = workManager,
                            tag = "BEST_JSON"
                        )
                    }
                },
                btnText = "JSON WORKER 취소"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "BEST_JSON"
                    )
                },
                btnText = "JSON WORKER 확인"
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}