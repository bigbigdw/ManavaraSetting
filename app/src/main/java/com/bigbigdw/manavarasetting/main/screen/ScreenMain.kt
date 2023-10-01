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
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.color1E4394
import com.bigbigdw.manavarasetting.ui.theme.color21C2EC
import com.bigbigdw.manavarasetting.ui.theme.color2EA259
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
import com.bigbigdw.manavarasetting.ui.theme.color808CF8
import com.bigbigdw.manavarasetting.ui.theme.color80BF78
import com.bigbigdw.manavarasetting.ui.theme.color8AA6BD
import com.bigbigdw.manavarasetting.ui.theme.color8F8F8F
import com.bigbigdw.manavarasetting.ui.theme.color91CEC7
import com.bigbigdw.manavarasetting.ui.theme.color998DF9
import com.bigbigdw.manavarasetting.ui.theme.colorABD436
import com.bigbigdw.manavarasetting.ui.theme.colorDCDCDD
import com.bigbigdw.manavarasetting.ui.theme.colorea927C
import com.bigbigdw.manavarasetting.ui.theme.colorF17666
import com.bigbigdw.manavarasetting.ui.theme.colorF17FA0
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.ui.theme.colorFDC24E
import com.bigbigdw.manavarasetting.ui.theme.colorFFAC59

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

    if(!isExpandedScreen){
        ScreenMainMobile(
            navController = navController,
            currentRoute = currentRoute,
            workManager = workManager,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    } else {
        ScreenMainTablet(
            navController = navController,
            currentRoute = currentRoute,
            workManager = workManager,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    }
}

@Composable
fun ScreenMainTablet(
    currentRoute : String?,
    navController: NavHostController,
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {
    Row{
//        TableAppNavRail(currentRoute = currentRoute ?: "", navController = navController)
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
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
){

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

    val lineTest = listOf(
        MainSettingLine(title = "WORKER : ", value = viewModelMain.state.collectAsState().value.statusTest),
        MainSettingLine(title = "갱신시간 : ", value = dataStore.getDataStoreString(DataStoreManager.TEST_TIME).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 횟수 : ", value = dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_TEST).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "금일 호출 횟수 : ", dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_TEST_TODAY).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 주기 : ", value = dataStore.getDataStoreString(DataStoreManager.TIMEMILL_TEST).collectAsState(initial = "").value ?: ""),
    )

    val lineBest = listOf(
        MainSettingLine(title = "WORKER : ", value = viewModelMain.state.collectAsState().value.statusBest),
        MainSettingLine(title = "갱신시간 : ", value = dataStore.getDataStoreString(DataStoreManager.BESTWORKER_TIME).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 횟수 : ", value = dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_BEST).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "금일 호출 횟수 : ", dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_BEST_TODAY).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 주기 : ", value = dataStore.getDataStoreString(DataStoreManager.TIMEMILL_BEST).collectAsState(initial = "").value ?: ""),
    )

    val lineJson = listOf(
        MainSettingLine(title = "WORKER : ", value = viewModelMain.state.collectAsState().value.statusJson),
        MainSettingLine(title = "갱신시간 : ", value = dataStore.getDataStoreString(DataStoreManager.JSONWORKER_TIME).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 횟수 : ", value = dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_JSON).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "금일 호출 횟수 : ", dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_JSON_TODAY).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 주기 : ", value = dataStore.getDataStoreString(DataStoreManager.TIMEMILL_JSON).collectAsState(initial = "").value ?: ""),
    )

    val lineTrophy = listOf(
        MainSettingLine(title = "WORKER : ", value = viewModelMain.state.collectAsState().value.statusTrophy),
        MainSettingLine(title = "갱신시간 : ", value = dataStore.getDataStoreString(DataStoreManager.TROPHYWORKER_TIME).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 횟수 : ", value = dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_TROPHY).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "금일 호출 횟수 : ", dataStore.getDataStoreString(DataStoreManager.FCM_COUNT_TROPHY_TODAY).collectAsState(initial = "").value ?: ""),
        MainSettingLine(title = "호출 주기 : ", value = dataStore.getDataStoreString(DataStoreManager.TIMEMILL_TROPHY).collectAsState(initial = "").value ?: ""),
    )

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.SETTING.screenRoute
    ) {
        composable(ScreemBottomItem.SETTING.screenRoute) {
            ScreenMainSetting(
                viewModelMain = viewModelMain,
                isExpandedScreen = isExpandedScreen,
                lineTest = lineTest,
                lineBest = lineBest,
                lineJson = lineJson,
                lineTrophy = lineTrophy
            )
        }
        composable(ScreemBottomItem.FCM.screenRoute) {
            ScreenMainFCM(lineTest = lineTest)
        }
        composable(ScreemBottomItem.BEST.screenRoute) {
            ScreenMainBest(lineBest = lineBest)
        }
        composable(ScreemBottomItem.JSON.screenRoute) {
            ScreenMainJson(lineJson = lineJson)
        }
        composable(ScreemBottomItem.TROPHY.screenRoute) {
            ScreenMainTrophy(lineTrophy = lineTrophy)
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
fun ScreenTableList(setMenu: (String) -> Unit, getMenu: String, onClick : () -> Unit){

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
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4AD7CF,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 관리",
            body = "FCM 테스트 & 공지사항 등록 & 토큰 획득",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color5372DE,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 공지사항 리스트",
            body = "NOTICE 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color998DF9,
            image = R.drawable.icon_fcm_wht,
            title = "FCM 알림 리스트",
            body = "ALERT 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorea927C,
            image = R.drawable.icon_best_wht,
            title = "베스트 리스트 관리",
            body = "베스트 리스트 수동 갱신 & Worker",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
            image = R.drawable.icon_best_wht,
            title = "베스트 BOOK 리스트",
            body = "장르별 작품 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17FA0,
            image = R.drawable.icon_best_wht,
            title = "베스트 최신화 현황",
            body = "시간별 베스트 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color21C2EC,
            image = R.drawable.icon_json_wht,
            title = "JSON 관리",
            body = "JSON 베스트 수동 갱신 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color31C3AE,
            image = R.drawable.icon_json_wht,
            title = "JSON 투데이 베스트 현황",
            body = "WEEK 투데이 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color7C81FF,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 베스트 현황",
            body = "WEEK 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color64C157,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 베스트 현황",
            body = "MONTH 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17666,
            image = R.drawable.icon_json_wht,
            title = "JSON 주간 트로피 현황",
            body = "장르별 주간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_json_wht,
            title = "JSON 월간 트로피 현황",
            body = "장르별 월간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color4996E8,
            image = R.drawable.icon_json_wht,
            title = "JSON 최신화 현황",
            body = "시간별 JSON 갱신 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorFDC24E,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 정산 관리",
            body = "트로피 수동 정산 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color80BF78,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 주간 토탈 리스트",
            body = "장르별 주간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color91CEC7,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 월간 토탈 리스트",
            body = "장르별 월간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

        ItemMainSettingSingleTablet(
            containerColor = color79B4F8,
            image = R.drawable.icon_trophy_wht,
            title = "트로피 최신화 현황",
            body = "시간별 트로피 최신화 현황",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = {onClick()}
        )

    }
}