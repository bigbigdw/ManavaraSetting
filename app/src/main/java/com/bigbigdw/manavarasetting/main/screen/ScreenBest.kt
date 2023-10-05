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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.NaverSeriesNovelGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenreKor
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMainBest(
    lineBest: List<MainSettingLine>,
    viewModelMain: ViewModelMain
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

            ContentsBest(viewModelMain = viewModelMain, lineBest = lineBest)
        }
    }
}

@Composable
fun ContentsBest(lineBest: List<MainSettingLine>, viewModelMain: ViewModelMain) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val itemNaverSeriesComic = listOf(
        MainSettingLine(title = "WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 3,
                tag = "BEST",
                timeMill = TimeUnit.HOURS,
                platform = "NAVER_SERIES",
                type = "COMIC"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
        MainSettingLine(title = "WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "BEST",
                platform = "NAVER_SERIES",
                type = "COMIC"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
        MainSettingLine(title = "WORKER 확인", onClick = {
            viewModelMain.checkWorker(
                workManager = workManager,
                tag = "BEST",
                platform = "NAVER_SERIES",
                type = "NOVEL"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
    )

    val itemNaverSeriesNovel = listOf(
        MainSettingLine(title = "WORKER 시작", onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 3,
                tag = "BEST",
                timeMill = TimeUnit.HOURS,
                platform = "NAVER_SERIES",
                type = "NOVEL"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
        MainSettingLine(title = "WORKER 취소", onClick = {
            PeriodicWorker.cancelWorker(
                workManager = workManager,
                tag = "BEST",
                platform = "NAVER_SERIES",
                type = "NOVEL"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
        MainSettingLine(title = "WORKER 확인", onClick = {
            viewModelMain.checkWorker(
                workManager = workManager,
                tag = "BEST",
                platform = "NAVER_SERIES",
                type = "NOVEL"
            )

            viewModelMain.getDataStoreStatus(context = context)
        }),
    )

    ItemTabletTitle(str = "네이버 시리즈 웹툰", isTopPadding = false)

    TabletContentWrap {
        itemNaverSeriesComic.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemNaverSeriesComic.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "네이버 시리즈 웹소설")

    TabletContentWrap {
        itemNaverSeriesNovel.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                isLast = itemNaverSeriesNovel.size - 1 == index,
                onClick = item.onClick
            )
        }
    }

    ItemTabletTitle(str = "베스트 현황")

    TabletContentWrap {
        lineBest.forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = lineBest.size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestList(
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailGenre: (String) -> Unit,
    setDetailType: (String) -> Unit,
) {

    val context = LocalContext.current

    TabletContentWrapBtn(
        onClick = {

            runBlocking {
                for (j in NaverSeriesComicGenre) {

                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_WEEK").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.miningNaverSeriesComic(pageCount = 1, genre = j)
                }
            }

            FCM.postFCMAlertTest(context = context, message = "베스트 리스트가 갱신되었습니다")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "네이버 시리즈 웹툰 수동 갱신",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        },
        isContinue = true
    )

    TabletContentWrapBtn(
        onClick = {

            for (j in NaverSeriesNovelGenre) {

                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "NOVEL").child("TROPHY_WEEK")
                        .removeValue()
                }

                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "NOVEL").child("TROPHY_WEEK")
                        .removeValue()
                }

                for (i in 1..5) {
                    MiningSource.miningNaverSeriesNovel(pageCount = i, genre = j)
                }
            }
            FCM.postFCMAlertTest(context = context, message = "베스트 리스트가 갱신되었습니다")
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "네이버 시리즈 웹소설 수동 갱신",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        },
        isContinue = false
    )

    ItemTabletTitle(str = "네이버 시리즈 웹툰")

    TabletContentWrap {
        NaverSeriesComicGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "네이버 시리즈 ${getNaverSeriesGenreKor(NaverSeriesComicGenre[index])}",
                isLast = NaverSeriesComicGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("NAVER_SERIES 베스트 리스트 ${getNaverSeriesGenre(NaverSeriesComicGenre[index])}")
                    setDetailPlatform("NAVER_SERIES")
                    setDetailGenre(getNaverSeriesGenre(NaverSeriesComicGenre[index]))
                    setDetailType("COMIC")
                }
            )
        }
    }

    ItemTabletTitle(str = "네이버 시리즈 소설")

    TabletContentWrap {
        NaverSeriesNovelGenre.forEachIndexed { index, _ ->
            ItemMainTabletContent(
                title = "네이버 시리즈 ${getNaverSeriesGenreKor(NaverSeriesNovelGenre[index])}",
                isLast = NaverSeriesNovelGenre.size - 1 == index,
                onClick = {
                    setDetailPage(true)
                    setDetailMenu("NAVER_SERIES 베스트 리스트 ${getNaverSeriesGenre(NaverSeriesNovelGenre[index])}")
                    setDetailPlatform("NAVER_SERIES")
                    setDetailGenre(getNaverSeriesGenre(NaverSeriesNovelGenre[index]))
                    setDetailType("NOVEL")
                }
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}
