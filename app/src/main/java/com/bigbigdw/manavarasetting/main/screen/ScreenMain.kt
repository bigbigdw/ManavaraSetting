package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.bigbigdw.manavarasetting.ui.theme.color1e4394
import com.bigbigdw.manavarasetting.ui.theme.color20459e
import com.bigbigdw.manavarasetting.ui.theme.color555b68
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colordcdcdd
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMain(
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    widthSizeClass: WindowWidthSizeClass
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded

    if(!isExpandedScreen){
        ScreenMainMobile(
            navController = navController,
            currentRoute = currentRoute,
            workManager = workManager,
            viewModelMain = viewModelMain,
            isExpandedScreen = isExpandedScreen
        )
    } else {

        Row{
            TableAppNavRail(currentRoute = currentRoute ?: "", navController = navController)
            NavigationGraph(
                navController = navController,
                workManager = workManager,
                viewModelMain = viewModelMain,
                isExpandedScreen = isExpandedScreen
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainMobile(
    navController: NavHostController,
    currentRoute: String?,
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
){
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
                workManager = workManager,
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
        backgroundColor = colordcdcdd,
        contentColor = color1e4394
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
                        fontFamily = pretendardvariable,
                        color = color1e4394
                    )
                },
                selected = currentRoute == item.screenRoute,
                selectedContentColor = color1e4394,
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
    workManager: WorkManager,
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean
) {

    NavHost(
        navController = navController,
        startDestination = ScreemBottomItem.SETTING.screenRoute
    ) {
        composable(ScreemBottomItem.SETTING.screenRoute) {
            ScreenMainSetting(workManager = workManager, viewModelMain = viewModelMain, isExpandedScreen = isExpandedScreen)
        }
        composable(ScreemBottomItem.FCM.screenRoute) {
            ScreenMainFCM(workManager = workManager, viewModelMain = viewModelMain, isExpandedScreen = isExpandedScreen)
        }
        composable(ScreemBottomItem.BEST.screenRoute) {
            ScreenMainBest(workManager = workManager, viewModelMain = viewModelMain, isExpandedScreen = isExpandedScreen)
        }
        composable(ScreemBottomItem.JSON.screenRoute) {
            ScreenMainJson(workManager = workManager, viewModelMain = viewModelMain, isExpandedScreen = isExpandedScreen)
        }
        composable(ScreemBottomItem.TROPHY.screenRoute) {
            ScreenMainTrophy(workManager = workManager, viewModelMain = viewModelMain, isExpandedScreen = isExpandedScreen)
        }
    }
}

@Composable
fun MainHeader(image: Int, title: String) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
    Card(
        modifier = Modifier
            .wrapContentSize(),
        backgroundColor = colordcdcdd,
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
        color = color000000,
        fontFamily = pretendardvariable,
        fontWeight = FontWeight(weight = 100)
    )
    Spacer(modifier = Modifier.size(22.dp))
}

@Composable
fun ItemMainSetting(
    image: Int,
    titleWorker: String,
    valueWorker: String,
    statusTitle: String,
    valueStatus: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = titleWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color000000,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = statusTitle,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color000000,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueStatus,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
    }
}

@Composable
fun BtnMobile(func : ()->Unit, btnText : String){
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
    )
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color20459e),
        onClick = func,
        modifier = Modifier
            .width(260.dp)
            .height(56.dp),
        shape = RoundedCornerShape(50.dp)

    ) {
        Text(
            text = btnText,
            textAlign = TextAlign.Center,
            color = colorEDE6FD,
            fontSize = 16.sp,
            fontFamily = pretendardvariable
        )
    }
}

@Composable
fun ItemMainSettingSingle(
    image: Int,
    titleWorker: String,
    valueWorker: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 0.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .height(16.dp)
                .width(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = titleWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color000000,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
        Text(
            text = valueWorker,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = color20459e,
            fontFamily = pretendardvariable,
            fontWeight = FontWeight(weight = 100)
        )
    }
}