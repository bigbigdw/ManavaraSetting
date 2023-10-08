package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.PeriodicWorker

@Composable
fun ScreenTablet(
    title: String,
    viewModelMain: ViewModelMain,
    setDetailPage: (Boolean) -> Unit,
    setDetailMenu: (String) -> Unit,
    setDetailPlatform: (String) -> Unit,
    setDetailType: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
            .background(color = colorF6F6F6)
            .semantics { contentDescription = "Overview Screen" },
    ) {

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            Text(
                modifier = Modifier.clickable {
                        setDetailPage(false)
                    },
                text = title,
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )
        }

        item { Spacer(modifier = Modifier.size(16.dp)) }

        item {
            when (title) {
                "세팅바라 현황" -> {
                    ContentsSetting()
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
                "웹소설 베스트 리스트" -> {
                    ContentsBestListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 베스트 리스트" -> {
                    ContentsBestListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 투데이" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 투데이 베스트",
                    )
                }
                "웹소설 JSON 투데이" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 투데이 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 JSON 주간" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 주간" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 주간 베스트",
                    )
                }
                "웹소설 JSON 월간" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 베스트",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 월간" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "JSON 월간 베스트",
                    )
                }
                "웹소설 JSON 주간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 주간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 주간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 JSON 월간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 JSON 월간 트로피" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "JSON 월간 트로피",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹소설 트로피 주간 토탈" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 주간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 트로피 주간 토탈" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 주간",
                    )
                }
                "웹소설 트로피 월간 토탈" -> {
                    ContentsBestJsonListNovel(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        type = "트로피 월간",
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                    )
                }
                "웹툰 트로피 월간 토탈" -> {
                    ContentsBestJsonListComic(
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType,
                        type = "트로피 월간",
                    )
                }
                "네이버 시리즈 웹툰" -> {
                    ContentsPlatformNaverSeriesComic(viewModelMain = viewModelMain)
                }
                "네이버 시리즈 웹소설" -> {
                    ContentsPlatformNaverSeriesNovel(viewModelMain = viewModelMain)
                }
                "조아라 웹소설" -> {
                    ContentsPlatformJoaraNovel(viewModelMain = viewModelMain)
                }
                "조아라 노블레스 웹소설" -> {
                    ContentsPlatformJoaraNoblessNovel(viewModelMain = viewModelMain)
                }
                "조아라 프리미엄 웹소설" -> {
                    ContentsPlatformJoaraPremiumNovel(viewModelMain = viewModelMain)
                }
                "챌린지 리그 웹소설" -> {
                    ContentsPlatformChallengeNovel(viewModelMain = viewModelMain)
                }
                "위험 옵션" -> {
                    ContentsDangerOption(viewModelMain = viewModelMain)
                }
                "실험실" -> {
                    ContentsDangerLabs()
                }
                "웹툰 현황" -> {
                    ContentsWebtoon()
                }
                "웹소설 현황" -> {
                    ContentsNovel()
                }
            }
        }
    }
}

@Composable
fun ContentsSetting() {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 3,
                tag = "MINING",
                platform = "NAVER_SERIES",
                type = "COMIC"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_naver),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "NAVER_SERIES COMIC",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 6,
                tag = "MINING",
                platform = "NAVER_SERIES",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_naver),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "NAVER_SERIES NOVEL",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 9,
                tag = "MINING",
                platform = "JOARA",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_joara),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "JOARA NOVEL",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 12,
                tag = "MINING",
                platform = "JOARA_NOBLESS",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_joara_nobless),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "JOARA_NOBLESS NOVEL",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 15,
                tag = "MINING",
                platform = "JOARA_PREMIUM",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_joara_premium),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "JOARA_PREMIUM NOVEL",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                delayMills = 18,
                tag = "MINING",
                platform = "NAVER_CHALLENGE",
                type = "NOVEL"
            )
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_naver_challenge),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "NAVER_CHALLENGE NOVEL",
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

    Spacer(modifier = Modifier.size(60.dp))
}