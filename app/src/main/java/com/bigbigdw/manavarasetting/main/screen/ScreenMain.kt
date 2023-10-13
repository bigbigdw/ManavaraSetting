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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bigbigdw.manavarasetting.ui.theme.colorCHALLENGE
import com.bigbigdw.manavarasetting.ui.theme.colorDCDCDD
import com.bigbigdw.manavarasetting.ui.theme.colorEA927C
import com.bigbigdw.manavarasetting.ui.theme.colorF17666
import com.bigbigdw.manavarasetting.ui.theme.colorF17FA0
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6
import com.bigbigdw.manavarasetting.ui.theme.colorFDC24E
import com.bigbigdw.manavarasetting.ui.theme.colorFFAC59
import com.bigbigdw.manavarasetting.ui.theme.colorJOARA
import com.bigbigdw.manavarasetting.ui.theme.colorKAKAO
import com.bigbigdw.manavarasetting.ui.theme.colorMUNPIA
import com.bigbigdw.manavarasetting.ui.theme.colorNAVER
import com.bigbigdw.manavarasetting.ui.theme.colorNOBLESS
import com.bigbigdw.manavarasetting.ui.theme.colorONESTORY
import com.bigbigdw.manavarasetting.ui.theme.colorPREMIUM
import com.bigbigdw.manavarasetting.ui.theme.colorRIDI
import com.bigbigdw.manavarasetting.ui.theme.colorTOKSODA
import com.bigbigdw.manavarasetting.util.BestRef
import com.bigbigdw.manavarasetting.util.DBDate
import com.bigbigdw.manavarasetting.util.MiningSource
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import kotlinx.coroutines.runBlocking

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
fun BottomNavScreen(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        ScreemBottomItem.SETTING,
        ScreemBottomItem.FCM,
        ScreemBottomItem.NOVEL,
        ScreemBottomItem.WEBTOON
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
        composable(ScreemBottomItem.NOVEL.screenRoute) {
            ScreenMainWebtoon()
        }
        composable(ScreemBottomItem.WEBTOON.screenRoute) {
            ScreenMainNovel()
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
fun ScreenTableList(setMenu: (String) -> Unit, getMenu: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(360.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(color = colorF6F6F6)
            .padding(8.dp, 0.dp)
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
            image = R.drawable.icon_novel_wht,
            title = "웹소설 현황",
            body = "웹소설 플랫폼 베스트 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorJOARA,
            image = R.drawable.logo_joara,
            title = "조아라 웹소설",
            body = "조아라 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )


        ItemMainSettingSingleTablet(
            containerColor = colorPREMIUM,
            image = R.drawable.logo_joara_premium,
            title = "조아라 프리미엄 웹소설",
            body = "조아라 프리미엄 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorNOBLESS,
            image = R.drawable.logo_joara_nobless,
            title = "조아라 노블레스 웹소설",
            body = "조아라 노블레스 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorNAVER,
            image = R.drawable.logo_naver,
            title = "네이버 시리즈 웹소설",
            body = "네이버 시리즈 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorNAVER,
            image = R.drawable.logo_naver,
            title = "네이버 웹소설 유료",
            body = "네이버 웹소설 유료 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorNAVER,
            image = R.drawable.logo_naver,
            title = "네이버 웹소설 무료",
            body = "네이버 웹소설 무료 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorCHALLENGE,
            image = R.drawable.logo_naver_challenge,
            title = "챌린지 리그",
            body = "네이버 챌린지 리그 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorCHALLENGE,
            image = R.drawable.logo_naver_challenge,
            title = "베스트 리그",
            body = "네이버 베스트 리그 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorRIDI,
            image = R.drawable.logo_ridibooks,
            title = "리디 판타지 웹소설",
            body = "리디북스 판타지 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorRIDI,
            image = R.drawable.logo_ridibooks,
            title = "리디 로맨스 웹소설",
            body = "리디북스 로맨스 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorONESTORY,
            image = R.drawable.logo_onestore,
            title = "원스토리 판타지 웹소설",
            body = "원스토리 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorONESTORY,
            image = R.drawable.logo_onestore,
            title = "원스토리 로맨스 웹소설",
            body = "원스토리 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorONESTORY,
            image = R.drawable.logo_onestore,
            title = "원스토리 PASS 판타지 웹소설",
            body = "원스토리 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorONESTORY,
            image = R.drawable.logo_onestore,
            title = "원스토리 PASS 로맨스 웹소설",
            body = "원스토리 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorKAKAO,
            image = R.drawable.logo_kakaostage,
            title = "카카오 스테이지 웹소설",
            body = "카카오 스테이지 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorMUNPIA,
            image = R.drawable.logo_munpia,
            title = "문피아 유료 웹소설",
            body = "문피아 유료 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorMUNPIA,
            image = R.drawable.logo_munpia,
            title = "문피아 무료 웹소설",
            body = "문피아 무료 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorTOKSODA,
            image = R.drawable.logo_toksoda,
            title = "톡소다 웹소설",
            body = "톡소다 웹소설 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorTOKSODA,
            image = R.drawable.logo_toksoda,
            title = "톡소다 자유연재",
            body = "톡소다 자유연재 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
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
            title = "투데이 장르 베스트",
            body = "투데이 장르 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
            image = R.drawable.icon_best_wht,
            title = "주간 장르 베스트",
            body = "투데이 장르 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorABD436,
            image = R.drawable.icon_best_wht,
            title = "월간 장르 베스트",
            body = "투데이 장르 베스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17FA0,
            image = R.drawable.icon_json_wht,
            title = "웹소설 JSON 투데이",
            body = "웹소설 플랫폼 JSON 베스트 투데이",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color21C2EC,
            image = R.drawable.icon_json_wht,
            title = "웹소설 JSON 주간",
            body = "웹소설 플랫폼 JSON 베스트 주간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color31C3AE,
            image = R.drawable.icon_json_wht,
            title = "웹소설 JSON 월간",
            body = "웹툰 플랫폼 JSON 베스트 월간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color7C81FF,
            image = R.drawable.icon_json_wht,
            title = "웹소설 JSON 주간 트로피",
            body = "웹소설 플랫폼 JSON 주간 트로피",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color64C157,
            image = R.drawable.icon_json_wht,
            title = "웹소설 JSON 월간 트로피",
            body = "웹툰 플랫폼 JSON 베스트 월간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorF17666,
            image = R.drawable.icon_trophy_wht,
            title = "웹소설 트로피 주간 토탈",
            body = "트로피 수동 정산 & Worker 관리",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_trophy_wht,
            title = "웹소설 트로피 월간 토탈",
            body = "장르별 월간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_trophy_wht,
            title = "투데이 장르 JSON",
            body = "투데이 장르 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_trophy_wht,
            title = "주간 장르 JSON",
            body = "주간 장르 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color536FD2,
            image = R.drawable.icon_trophy_wht,
            title = "월간 장르 JSON",
            body = "월간 장르 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        TabletBorderLine()

        ItemMainSettingSingleTablet(
            containerColor = color4996E8,
            image = R.drawable.icon_webtoon_wht,
            title = "웹툰 현황",
            body = "웹툰 플랫폼 베스트 리스트 확인",
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
            containerColor = colorFDC24E,
            image = R.drawable.icon_best_wht,
            title = "웹툰 베스트 리스트",
            body = "웹툰 플랫폼 베스트 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color80BF78,
            image = R.drawable.icon_json_wht,
            title = "웹툰 JSON 투데이",
            body = "웹툰 플랫폼 JSON 베스트 투데이",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color91CEC7,
            image = R.drawable.icon_json_wht,
            title = "웹툰 JSON 주간",
            body = "웹툰 플랫폼 JSON 베스트 주간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )
        ItemMainSettingSingleTablet(
            containerColor = color79B4F8,
            image = R.drawable.icon_json_wht,
            title = "웹툰 JSON 월간",
            body = "웹툰 플랫폼 JSON 베스트 월간",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color8AA6BD,
            image = R.drawable.icon_json_wht,
            title = "웹툰 JSON 주간 트로피",
            body = "웹툰 플랫폼 JSON 주간 트로피",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color2EA259,
            image = R.drawable.icon_json_wht,
            title = "웹툰 JSON 월간 트로피",
            body = "장르별 월간 JSON 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = color808CF8,
            image = R.drawable.icon_trophy_wht,
            title = "웹툰 트로피 주간 토탈",
            body = "장르별 주간 트로피 리스트 확인",
            setMenu = setMenu,
            getMenu = getMenu,
            onClick = { onClick() }
        )

        ItemMainSettingSingleTablet(
            containerColor = colorFFAC59,
            image = R.drawable.icon_trophy_wht,
            title = "웹툰 트로피 월간 토탈",
            body = "시간별 트로피 최신화 현황",
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
fun ContentsLabs() {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

//    getJsonGenreWeekList(platform = "JOARA", type = "NOVEL", menu = "주간")

    TabletContentWrapBtn(
        onClick = {
            runBlocking {
                if (DBDate.getDayOfWeekAsNumber() == 0) {
                    BestRef.setBestRef(
                        platform = "TOKSODA",
                        type = "NOVEL"
                    )
                        .child("TROPHY_MONTH").removeValue()
                }

                if (DBDate.datedd() == "01") {
                    BestRef.setBestRef(platform = "TOKSODA", type = "NOVEL")
                        .child("TROPHY_MONTH").removeValue()
                }

                MiningSource.mining(
                    platform = "TOKSODA",
                    type = "NOVEL",
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
                Text(
                    text = "테스트",
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
                delayMills = 3,
                tag = "NOVEL",
                platform = "ALL",
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
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "마나바라 NOVEL 전체",
                    color = color000000,
                    fontSize = 18.sp,
                )
            }
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}