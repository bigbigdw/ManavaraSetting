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
        isInit = true
    }

    Box(
        modifier = Modifier.fillMaxSize().background(color = colorF6F6F6)
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
                ScreenSettingMobile()
            }
        }
    }
}

@Composable
fun ScreenSettingMobile() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorF6F6F6)
            .verticalScroll(rememberScrollState())
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
                platform = "NAVER_WEBNOVEL_PAY",
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
                    text = "네이버 웹소설 유료 NOVEL",
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
                platform = "NAVER_WEBNOVEL_FREE",
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
                    text = "네이버 웹소설 무료 NOVEL",
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
                platform = "RIDI_FANTAGY",
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
                    painter = painterResource(id = R.drawable.logo_ridibooks),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "리디 판타지 NOVEL",
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
                delayMills = 21,
                tag = "MINING",
                platform = "RIDI_ROMANCE",
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
                    painter = painterResource(id = R.drawable.logo_ridibooks),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "리디 로맨스 NOVEL",
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
                delayMills = 24,
                tag = "MINING",
                platform = "ONESTORY_FANTAGY",
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
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "원스토리 판타지 NOVEL",
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
                delayMills = 24,
                tag = "MINING",
                platform = "ONESTORY_ROMANCE",
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
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "원스토리 로맨스 NOVEL",
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
                delayMills = 24,
                tag = "MINING",
                platform = "ONESTORY_PASS_FANTAGY",
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
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "원스토리 PASS 판타지 NOVEL",
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
                delayMills = 24,
                tag = "MINING",
                platform = "ONESTORY_PASS_ROMANCE",
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
                    painter = painterResource(id = R.drawable.logo_onestore),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "원스토리 PASS 로맨스 NOVEL",
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
                delayMills = 27,
                tag = "MINING",
                platform = "KAKAO_STAGE",
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
                    painter = painterResource(id = R.drawable.logo_kakaostage),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "카카오 스테이지 NOVEL",
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
                delayMills = 30,
                tag = "MINING",
                platform = "MUNPIA_PAY",
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
                    painter = painterResource(id = R.drawable.logo_munpia),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "문피아 유료 NOVEL",
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
                delayMills = 30,
                tag = "MINING",
                platform = "MUNPIA_FREE",
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
                    painter = painterResource(id = R.drawable.logo_munpia),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "문피아 무료 NOVEL",
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
                delayMills = 33,
                tag = "MINING",
                platform = "TOKSODA",
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
                    painter = painterResource(id = R.drawable.logo_toksoda),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "톡소다 NOVEL",
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
                delayMills = 33,
                tag = "MINING",
                platform = "TOKSODA_FREE",
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
                    painter = painterResource(id = R.drawable.logo_toksoda),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "톡소다 자유연재 NOVEL",
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
                delayMills = 36,
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