package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.ChallengeGenre
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getDataStoreStatus
import com.bigbigdw.manavarasetting.util.getJoaraNoblessNovelArray
import com.bigbigdw.manavarasetting.util.getJoaraNovelArray
import com.bigbigdw.manavarasetting.util.getJoaraPremiumNovelArray
import com.bigbigdw.manavarasetting.util.getNaverChallengeNovelArray
import com.bigbigdw.manavarasetting.util.getNaverSeriesComicArray

import java.util.concurrent.TimeUnit


@Composable
fun ContentsPlatformNaverSeriesComic(viewModelMain: ViewModelMain) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    viewModelMain.getFCMList(child = "ALERT", activity = "NAVER_SERIES COMIC")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_NAVER_SERIES_COMIC
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getNaverSeriesComicArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getNaverSeriesComicArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformNaverSeriesNovel(viewModelMain: ViewModelMain) {

    viewModelMain.getFCMList(child = "ALERT", activity = "NAVER_SERIES NOVEL")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_NAVER_SERIES_NOVEL
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getJoaraNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getJoaraNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformJoaraNovel(viewModelMain : ViewModelMain) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    viewModelMain.getFCMList(child = "ALERT", activity = "JOARA NOVEL")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_NAVER_SERIES_NOVEL
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getJoaraPremiumNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getJoaraPremiumNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformJoaraPremiumNovel(viewModelMain : ViewModelMain) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    viewModelMain.getFCMList(child = "ALERT", activity = "JOARA_PREMIUM NOVEL")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_JOARA_PREMIUM_NOVEL
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getJoaraPremiumNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getJoaraPremiumNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformJoaraNoblessNovel(viewModelMain : ViewModelMain) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    viewModelMain.getFCMList(child = "ALERT", activity = "JOARA_NOBLESS NOVEL")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_JOARA_NOBLESS_NOVEL
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getJoaraNoblessNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getJoaraNoblessNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformChallengeNovel(viewModelMain : ViewModelMain) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val workManager = WorkManager.getInstance(context)
    val dataStore = DataStoreManager(context)

    viewModelMain.getFCMList(child = "ALERT", activity = "NAVER_CHALLENGE NOVEL")
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING",
                timeMill = TimeUnit.MINUTES,
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
        onClick = {},
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = spannableString(
                    textFront = "최신화 현황 : ", color = color000000,
                    textEnd = dataStore.getDataStoreString(
                        DataStoreManager.MINING_JOARA_NOBLESS_NOVEL
                    ).collectAsState(initial = "").value ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

    TabletContentWrap {
        getNaverChallengeNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getNaverChallengeNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(16.dp))

    TabletContentWrap {
        Spacer(modifier = Modifier.size(8.dp))

        fcmAlertList.forEachIndexed { index, item ->
            ItemTabletFCMList(
                item = item,
                isLast = fcmAlertList.size - 1 == index
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
    }

    Spacer(modifier = Modifier.size(60.dp))
}