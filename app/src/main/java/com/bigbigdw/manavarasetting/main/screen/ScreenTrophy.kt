package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.NaverSeriesGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainTrophy(
    lineTrophy: List<MainSettingLine>
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf6f6f6)
                .padding(16.dp, 0.dp)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainHeader(image = R.drawable.icon_trophy, title = "트로피 최신화 현황")

            ContentsTrophy(lineTrophy = lineTrophy)
        }
    }
}

@Composable
fun ContentsTrophy(lineTrophy: List<MainSettingLine>) {

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