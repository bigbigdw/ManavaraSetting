package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color20459E
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.doMining
import com.bigbigdw.manavarasetting.util.getDataStoreStatus
import kotlinx.coroutines.runBlocking


@Composable
fun ScreenMainWebtoon() {

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

            MainHeader(image = R.drawable.icon_novel, title = "웹소설 현황")

            ContentsNovel()

        }
    }
}

@Composable
fun ScreenMainNovel() {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorF6F6F6)
                .padding(16.dp, 0.dp)
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            MainHeader(image = R.drawable.icon_webtoon, title = "웹툰 현황")

            ContentsWebtoon()

        }
    }
}

@Composable
fun ContentsNovel() {

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    getDataStoreStatus(context = context, update = {})

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "조아라 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_JOARA_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "조아라 프리미엄 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_JOARA_PREMIUM_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "조아라 노블레스 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_JOARA_NOBLESS_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 시리즈 웹소설 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_SERIES_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 챌린지 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_CHALLENGE_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )


    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 베스트 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_BEST_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 웹소설 유료 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_WEBNOVEL_PAY_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )


    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 웹소설 무료 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_WEBNOVEL_FREE_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = spannableString(
                        textFront = "원스토리 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_ONESTORY_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_kakaostage),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = spannableString(
                        textFront = "카카오 스테이지 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_KAKAO_STAGE_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_munpia),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = spannableString(
                        textFront = "문피아 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_MUNPIA_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {},
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_toksoda),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = spannableString(
                        textFront = "톡소다 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_TOKSODA_NOVEL
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    ContentsLabs()

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsWebtoon() {

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    getDataStoreStatus(context = context, update = {})

    TabletContentWrapBtn(
        onClick = {},
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
                    text = spannableString(
                        textFront = "네이버 시리즈 웹툰 : ", color = color000000,
                        textEnd = dataStore.getDataStoreString(
                            DataStoreManager.MINING_NAVER_SERIES_COMIC
                        ).collectAsState(initial = "").value ?: "",
                    ),
                    color = color20459E,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatform(
    viewModelMain: ViewModelMain,
    platform: String,
    type: String,
    key: Preferences.Key<String>,
    logo: Int
) {

    val context = LocalContext.current
    getDataStoreStatus(context = context, update = {})
    val dataStore = DataStoreManager(context)

    viewModelMain.getMiningList(title = platform)
    val fcmAlertList = viewModelMain.state.collectAsState().value.fcmAlertList

    TabletContentWrapBtn(
        onClick = {
            runBlocking {
                doMining(
                    platform = platform,
                    type = type,
                    yesterDayItemMap = mutableMapOf(),
                    context = context
                )
            }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "$platform $type",
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
                    textEnd = dataStore.getDataStoreString(key).collectAsState(initial = "").value
                        ?: "",
                ),
                color = color000000,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
        }
    )

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

