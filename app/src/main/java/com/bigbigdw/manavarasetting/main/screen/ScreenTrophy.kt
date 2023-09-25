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
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainTrophy(
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

            MainHeader(image = R.drawable.icon_trophy, title = "트로피 갱신 현황")

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

            BtnMobile(
                func = {
                    for (j in NaverSeriesGenre) {
                        calculateTrophy(platform = "NAVER_SERIES", genre = getNaverSeriesGenre(j))
                    }
                    FCM.postFCMAlertTest(context = context, message = "트로피 정산이 완료되었습니다")
                },
                btnText = "트로피 정산"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 9,
                        tag = "BEST_TROPHY",
                        timeMill = TimeUnit.HOURS
                    )
                },
                btnText = "트로피 정산 WORKER 시작"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.cancelWorker(
                        workManager = workManager,
                        tag = "BEST_TROPHY"
                    )
                },
                btnText = "트로피 정산 WORKER 취소"
            )

            BtnMobile(
                func = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "BEST_TROPHY"
                    )
                },
                btnText = "트로피 정산 WORKER 확인"
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}