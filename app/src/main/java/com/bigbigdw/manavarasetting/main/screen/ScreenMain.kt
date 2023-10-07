package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.util.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.color1E4394
import com.bigbigdw.manavarasetting.ui.theme.color21C2EC
import com.bigbigdw.manavarasetting.ui.theme.color31C3AE
import com.bigbigdw.manavarasetting.ui.theme.color4996E8
import com.bigbigdw.manavarasetting.ui.theme.color4AD7CF
import com.bigbigdw.manavarasetting.ui.theme.color52A9FF
import com.bigbigdw.manavarasetting.ui.theme.color536FD2
import com.bigbigdw.manavarasetting.ui.theme.color5372DE
import com.bigbigdw.manavarasetting.ui.theme.color555b68
import com.bigbigdw.manavarasetting.ui.theme.color64C157
import com.bigbigdw.manavarasetting.ui.theme.color79B4F8
import com.bigbigdw.manavarasetting.ui.theme.color7C81FF
import com.bigbigdw.manavarasetting.ui.theme.color80BF78
import com.bigbigdw.manavarasetting.ui.theme.color8F8F8F
import com.bigbigdw.manavarasetting.ui.theme.color91CEC7
import com.bigbigdw.manavarasetting.ui.theme.color998DF9
import com.bigbigdw.manavarasetting.ui.theme.colorABD436
import com.bigbigdw.manavarasetting.ui.theme.colorDCDCDD
import com.bigbigdw.manavarasetting.ui.theme.colorEA927C
import com.bigbigdw.manavarasetting.ui.theme.colorF17666
import com.bigbigdw.manavarasetting.ui.theme.colorF17FA0
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.ui.theme.colorFDC24E
import com.bigbigdw.manavarasetting.ui.theme.colorNAVER
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.NaverSeriesComicGenre
import com.bigbigdw.manavarasetting.util.NaverSeriesNovelGenre
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import com.bigbigdw.manavarasetting.util.calculateTrophy
import com.bigbigdw.manavarasetting.util.getDataStoreStatus
import com.bigbigdw.manavarasetting.util.getNaverSeriesComicArray
import com.bigbigdw.manavarasetting.util.getNaverSeriesGenre
import com.bigbigdw.manavarasetting.util.getNaverSeriesNovelArray
import com.bigbigdw.manavarasetting.util.uploadJsonArrayToStorageDay
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Composable
fun ScreenMain(
    viewModelMain: ViewModelMain,
    widthSizeClass: WindowWidthSizeClass
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

//    if(isExpandedScreen){
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//    }

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    if (!isExpandedScreen) {
        ScreenMainMobile(
            navController = navController,
            currentRoute = currentRoute,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    } else {
        ScreenMainTablet(
            navController = navController,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    }
}

@Composable
fun ScreenMainTablet(
    navController: NavHostController,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {
    Row {
        NavigationGraph(
            navController = navController,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScreenMainMobile(
    navController: NavHostController,
    currentRoute: String?,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    Scaffold(
//        topBar = { MainTopBar() },
        bottomBar = { BottomNavScreen(navController = navController, currentRoute = currentRoute) }
    ) {
        Box(
            Modifier
                .padding(it)
                .background(color = color1E1E20)
                .fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController,
                viewModelMain = viewModelMain,
                isExpandedScreen = isExpandedScreen
            )
        }
    }

//    ModalBottomSheetLayout(
//        sheetState = modalSheetState,
//        sheetElevation = 50.dp,
//        sheetShape = RoundedCornerShape(
//            topStart = 25.dp,
//            topEnd = 25.dp
//        ),
//        sheetContent = {
//            ScreenTest()
//        },
//    ) {}
}

@Composable
fun MainTopBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .background(color = Color.Cyan)
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(22.dp)
                    .clickable { }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { }
        )

        Spacer(
            modifier = Modifier
                .wrapContentWidth()
                .width(16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .width(22.dp)
                .height(22.dp)
                .clickable { }
        )
    }
}

@Composable
fun BottomNavScreen(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        ScreemBottomItem.SETTING,
        ScreemBottomItem.FCM,
        ScreemBottomItem.BEST,
        ScreemBottomItem.JSON,
        ScreemBottomItem.TROPHY,
    )

    BottomNavigation(
        backgroundColor = colorDCDCDD,
        contentColor = color1E4394
    ) {

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = if (currentRoute == item.screenRoute) {
                            painterResource(id = item.iconOn)
                        } else {
                            painterResource(id = item.iconOff)
                        },
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        color = color1E4394
                    )
                },
                selected = currentRoute == item.screenRoute,
                selectedContentColor = color1E4394,
                unselectedContentColor = color555b68,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    val context = LocalContext.current
    val dataStore = DataStoreManager(context)

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.SETTING.screenRoute
    ) {
        composable(ScreemBottomItem.SETTING.screenRoute) {
            ScreenMainSetting(
                viewModelMain = viewModelMain,
                isExpandedScreen = isExpandedScreen,
            )
        }
        composable(ScreemBottomItem.FCM.screenRoute) {
            ScreenMainFCM()
        }
        composable(ScreemBottomItem.BEST.screenRoute) {
            ScreenMainBest(viewModelMain = viewModelMain)
        }
        composable(ScreemBottomItem.JSON.screenRoute) {
            ScreenMainJson(viewModelMain = viewModelMain)
        }
        composable(ScreemBottomItem.TROPHY.screenRoute) {
            ScreenMainTrophy(viewModelMain = viewModelMain)
        }
    }
}

@Composable
fun MainHeader(image: Int, title: String) {

    Card(
        modifier = Modifier
            .wrapContentSize(),
        backgroundColor = colorDCDCDD,
        shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
    ) {
        Box(
            modifier = Modifier
                .height(90.dp)
                .width(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .height(72.dp)
                    .width(72.dp)
            )
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
    )
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = color000000
    )
    Spacer(modifier = Modifier.size(22.dp))
}

@Composable
fun TableAppNavRail(
    currentRoute: String,
    navController: NavHostController
) {

    val items = listOf(
        ScreemBottomItem.SETTING,
        ScreemBottomItem.FCM,
        ScreemBottomItem.BEST,
        ScreemBottomItem.JSON,
        ScreemBottomItem.TROPHY,
    )

    NavigationRail(
        header = {
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
            )
        },
        modifier = Modifier.background(color = color1E1E20)
    ) {
        Spacer(Modifier.weight(1f))

        items.forEach { item ->
            NavigationRailItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    androidx.compose.material3.Icon(
                        painter = if (currentRoute == item.screenRoute) {
                            painterResource(id = item.iconOn)
                        } else {
                            painterResource(id = item.iconOff)
                        },
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        color = color1E4394
                    )
                },
                alwaysShowLabel = false
            )
        }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun ScreenTableList(setMenu: (String) -> Unit, getMenu: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(360.dp)
            .fillMaxHeight()
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
    ) {

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp),
            text = "세팅바라",
            fontSize = 24.sp,
            color = color000000,
            fontWeight = FontWeight(weight = 700)
        )

        Spacer(modifier = Modifier.size(16.dp))

        ItemMainSettingSingleTablet(
            containerColor = color52A9FF,
            image = R.drawable.ic_launcher,
            title = "세팅바라 현황",
            body = "Periodic Worker 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 관리",
            body = "FCM 테스트 & 공지사항 등록 & 토큰 획득",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 공지사항 리스트",
            body = "NOTICE 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 알림 리스트",
            body = "ALERT 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorEA927C,
            image = R.drawable.icon_best_wht,
            title = "웹소설 베스트 리스트",
            body = "웹소설 플랫폼 베스트 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
            image = R.drawable.icon_best_wht,
            title = "웹툰 베스트 리스트",
            body = "웹툰 플랫폼 베스트 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorF17FA0,
            image = R.drawable.icon_json_wht,
            title = "JSON 관리",
            body = "JSON 베스트 수동 갱신 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color21C2EC,
            image = R.drawable.icon_json_wht,
            title = "JSON 투데이 베스트 현황",
            body = "WEEK 투데이 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color7C81FF,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 베스트 현황",
            body = "WEEK 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color64C157,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 베스트 현황",
            body = "MONTH 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17666,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 트로피 현황",
            body = "장르별 주간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 트로피 현황",
            body = "장르별 월간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color4996E8,
            image = R.drawable.icon_json_wht,
            title = "JSON 최신화 현황",
            body = "시간별 JSON 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorFDC24E,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 정산 관리",
            body = "트로피 수동 정산 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color80BF78,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 주간 토탈 리스트",
            body = "장르별 주간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color91CEC7,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 월간 토탈 리스트",
            body = "장르별 월간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color79B4F8,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 최신화 현황",
            body = "시간별 트로피 최신화 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorNAVER,
            image = R.drawable.logo_naver,
            title = "네이버 시리즈 웹툰",
            body = "네이버 시리즈 웹툰 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorNAVER,
            image = R.drawable.logo_naver,
            title = "네이버 시리즈 웹소설",
            body = "네이버 시리즈 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color8F8F8F,
            image = R.drawable.icon_setting_wht,
            title = "위험 옵션",
            body = "건들면 돌이킬 수 없는 옵션 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color8F8F8F,
            image = R.drawable.icon_setting_wht,
            title = "실험실",
            body = "일단 기능 만들고 질러보는 공간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

    }
}

@Composable
fun ContentsDangerOption(viewModelMain: ViewModelMain) {

    TabletContentWrapBtn(
        onClick = { viewModelMain.resetBest(str = "ALERT") },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKER ALERT 초기화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = { viewModelMain.resetBest(str = "BEST") },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BEST 초기화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = { viewModelMain.resetBest(str = "BOOK") },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BOOK 초기화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = { viewModelMain.resetBest(str = "BOOK") },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DATA 초기화",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsDangerLabs(viewModelMain: ViewModelMain) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    TabletContentWrapBtn(
        onClick = {
            PeriodicWorker.doWorker(
                workManager = workManager,
                repeatInterval = 15,
                tag = "MINING_COMIC",
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
                Text(
                    text = "WORKER COMIC 실행",
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
                repeatInterval = 15,
                tag = "MINING_NOVEL",
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
                Text(
                    text = "WORKER NOVEL 실험",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = "COMIC"
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    val miningJobs = List(5) { i ->
                        async(threadPool) {
                            MiningSource.miningNaverSeriesComic(pageCount = i + 1, genre = j)
                        }
                    }

                    miningJobs.awaitAll()

                    launch {
                        uploadJsonArrayToStorageDay(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "COMIC"
                        )
                    }

                    launch {
                        calculateTrophy(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "COMIC"
                        )
                    }
                }
            }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKER COMIC 수동",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            val threadPool = Executors.newFixedThreadPool(5).asCoroutineDispatcher()

            runBlocking {
                for (j in NaverSeriesNovelGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = "NOVEL"
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "NOVEL")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    val miningJobs = List(5) { i ->
                        async(threadPool) {
                            MiningSource.miningNaverSeriesNovel(pageCount = i + 1, genre = j)
                        }
                    }

                    miningJobs.awaitAll()

                    launch {
                        uploadJsonArrayToStorageDay(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "NOVEL"
                        )
                    }

                    launch {
                        calculateTrophy(
                            platform = "NAVER_SERIES",
                            genre = getNaverSeriesGenre(j),
                            type = "NOVEL"
                        )
                    }
                }
            }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WORKER NOVEL 수동",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            viewModelMain.checkWorker(
                workManager = workManager,
                tag = "MINING_COMIC",
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
                Text(
                    text = "WORKER COMIC 체크",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {
            viewModelMain.checkWorker(
                workManager = workManager,
                tag = "MINING_NOVEL",
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
                Text(
                    text = "WORKER NOVEL 체크",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {

            runBlocking {
                for (j in NaverSeriesComicGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = "COMIC"
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "COMIC")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.mining(
                        genre = j,
                        genreDir = getNaverSeriesGenre(j),
                        platform = "NAVER_SERIES",
                        type = "COMIC"
                    )
                }
            }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NAVER_SERIES COMIC 원큐",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    TabletContentWrapBtn(
        onClick = {

            runBlocking {
                for (j in NaverSeriesNovelGenre) {
                    if (DBDate.getDayOfWeekAsNumber() == 0) {
                        BestRef.setBestRef(
                            platform = "NAVER_SERIES",
                            genre = j,
                            type = "NOVEL"
                        )
                            .child("TROPHY_MONTH").removeValue()
                    }

                    if (DBDate.datedd() == "01") {
                        BestRef.setBestRef(platform = "NAVER_SERIES", genre = j, type = "NOVEL")
                            .child("TROPHY_MONTH").removeValue()
                    }

                    MiningSource.mining(
                        genre = j,
                        platform = "NAVER_SERIES",
                        type = "NOVEL",
                        genreDir = getNaverSeriesGenre(j)
                    )
                }
            }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NAVER_SERIES NOVEL 원큐",
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
                Text(
                    text = "WORKER NAVER_SERIES COMIC 실행",
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
                Text(
                    text = "WORKER NAVER_SERIES NOVEL 실행",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformNaverSeriesComic() {

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

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsPlatformNaverSeriesNovel() {

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
        getNaverSeriesNovelArray(context).forEachIndexed { index, item ->
            ItemMainTabletContent(
                title = item.title,
                value = item.value,
                isLast = getNaverSeriesNovelArray(context).size - 1 == index
            )
        }
    }

    Spacer(modifier = Modifier.size(60.dp))
}