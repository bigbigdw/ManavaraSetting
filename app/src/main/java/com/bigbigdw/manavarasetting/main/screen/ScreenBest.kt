package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
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
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
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
        MainSettingLine(title = "WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 3,
                tag = "BEST",
                timeMill = TimeUnit.HOURS
            )
        }),
        MainSettingLine(title = "WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(workManager = workManager,  tag = "BEST")
        }),
        MainSettingLine(title = "WORKER 확인", onClick = {
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
