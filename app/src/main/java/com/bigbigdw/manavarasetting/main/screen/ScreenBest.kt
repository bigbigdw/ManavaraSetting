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
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainBest(
    lineBest: List<MainSettingLine>
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

            MainHeader(image = R.drawable.icon_best, title = "베스트 리스트 현황")

            ContentsBest(lineBest = lineBest)
        }
    }
}

@Composable
fun ContentsBest(lineBest: List<MainSettingLine>) {

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
