package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.getDataStoreStatus

@Composable
fun ScreenMainSetting(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    val context = LocalContext.current

    var isInit by remember { mutableStateOf(false) }

    if(!isInit){
        getDataStoreStatus(context = context, update = {})
        viewModelMain.getDataStoreFCMCount()
        isInit = true
    }

    Box(
        modifier = Modifier.fillMaxWidth().background(color = colorF6F6F6)
    ) {

        Row {
            if(isExpandedScreen){

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }
                val (getDetailMenu, setDetailMenu) = remember { mutableStateOf("") }
                val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf("") }
                val (getDetailType, setDetailType) = remember { mutableStateOf("") }

                ScreenTableList(setMenu = setMenu, getMenu = getMenu, onClick = {setDetailPage(false)})

                Spacer(modifier = Modifier.size(16.dp))

                if(getDetailPage){
                    ScreenTabletDetail(
                        setDetailPage = setDetailPage,
                        getDetailMenu = getDetailMenu,
                        viewModelMain = viewModelMain,
                        getDetailPlatform = getDetailPlatform,
                        getDetailType = getDetailType,
                    )
                } else {
                    ScreenTablet(
                        title = getMenu,
                        viewModelMain = viewModelMain,
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailType = setDetailType
                    )
                }

            } else {
                ScreenSettingMobile(
                    viewModelMain = viewModelMain,
                )
            }
        }
    }
}

@Composable
fun ScreenSettingMobile(
    viewModelMain: ViewModelMain,
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

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ContentsSetting()
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
                    text = "조아라 NOVEL",
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
                    text = "조아라 노블레스 NOVEL",
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
                    text = "조아라 프리미엄 NOVEL",
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
                    text = "네이버 시리즈 NOVEL",
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
                    text = "챌린지 리그 NOVEL",
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
                platform = "NAVER_BEST",
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
                    text = "베스트 리그 NOVEL",
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
                    text = "네이버 시리즈 COMIC",
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