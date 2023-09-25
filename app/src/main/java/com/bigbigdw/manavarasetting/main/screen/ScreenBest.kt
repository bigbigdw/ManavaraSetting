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
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.Mining
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainBest(
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

            MainHeader(image = R.drawable.icon_best, title = "베스트 리스트 현황")

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

            BtnMobile(
                func = {
                    for (j in NaverSeriesGenre) {

                        if (DBDate.getDayOfWeekAsNumber() == 0) {
                            BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_WEEK").removeValue()
                        }

                        if (DBDate.datedd() == "01") {
                            BestRef.setBestRef(platform = "NAVER_SERIES", genre = j).child("TROPHY_MONTH").removeValue()
                        }

                        for (i in 1..5) {
                            Mining.miningNaverSeriesAll(pageCount = i, genre = j)
                        }
                    }
                    FCM.postFCMAlertTest(context = context, message = "베스트 리스트가 갱신되었습니다")
                },
                btnText = "베스트 리스트 갱신"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 3,
                        tag = "BEST",
                        timeMill = TimeUnit.HOURS
                    )
                },
                btnText = "베스트 WORKER 시작"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.cancelWorker(workManager = workManager,  tag = "BEST")
                },
                btnText = "베스트 WORKER 취소"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "BEST"
                    )
                },
                btnText = "베스트 WORKER 확인"
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}