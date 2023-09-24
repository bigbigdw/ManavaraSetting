package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bigbigdw.manavarasetting.firebase.DataFCMBodyNotification
import com.bigbigdw.manavarasetting.main.viewModels.DataStoreManager
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color1E1E20
import com.bigbigdw.manavarasetting.ui.theme.color1e4394
import com.bigbigdw.manavarasetting.ui.theme.color20459e
import com.bigbigdw.manavarasetting.ui.theme.color555b68
import com.bigbigdw.manavarasetting.ui.theme.color898989
import com.bigbigdw.manavarasetting.ui.theme.colorEDE6FD
import com.bigbigdw.manavarasetting.ui.theme.colorFFFFFF
import com.bigbigdw.manavarasetting.ui.theme.colordcdcdd
import com.bigbigdw.manavarasetting.ui.theme.colorf7f7f7
import com.bigbigdw.manavarasetting.ui.theme.pretendardvariable
import com.bigbigdw.manavarasetting.util.FCM
import com.bigbigdw.manavarasetting.util.PeriodicWorker
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenMain(workManager: WorkManager, viewModelMain: ViewModelMain) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                .fillMaxSize()){
            NavigationGraph(navController = navController, workManager = workManager, viewModelMain = viewModelMain)
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetElevation = 50.dp,
        sheetShape = RoundedCornerShape(
            topStart = 25.dp,
            topEnd = 25.dp
        ),
        sheetContent = {
            ScreenTest()
        },
    ) {}
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

        Spacer(modifier = Modifier
            .wrapContentWidth()
            .width(16.dp))

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
                label = { Text( text = item.title, fontSize = 13.sp, fontFamily = pretendardvariable, color = color1e4394 ) },
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
    viewModelMain: ViewModelMain
) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ScreemBottomItem.SETTING.screenRoute) {
        composable(ScreemBottomItem.SETTING.screenRoute) {
            ScreenMainSetting(workManager = workManager, viewModelMain = viewModelMain)
        }
        composable(ScreemBottomItem.FCM.screenRoute) {
            ScreenMainFCM(workManager = workManager)
        }
        composable(ScreemBottomItem.BEST.screenRoute) {
            Test(workManager = workManager)
        }
        composable(ScreemBottomItem.JSON.screenRoute) {
            ScreenTest()
        }
        composable(ScreemBottomItem.TROPHY.screenRoute) {
            ScreenTest()
        }
    }
}

@Composable
fun ScreenMainSetting(workManager: WorkManager, viewModelMain: ViewModelMain) {

    val context = LocalContext.current

    viewModelMain.getDataStoreStatus(context = context)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf7f7f7)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 15,
                        tag = "TEST",
                        timeMill = TimeUnit.MINUTES
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 시작",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.cancelWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 취소",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 확인",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = { FCM.getFCMToken(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 토큰 얻기",
                    textAlign = TextAlign.Center,
                    color = colorFFFFFF,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = { FCM.postFCMAlertTest(context = context, message = "테스트") },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 테스트",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }



            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMainFCM(workManager: WorkManager) {
    val context = LocalContext.current

    val dataStore = DataStoreManager(context)
    val (getFCM, setFCM) = remember { mutableStateOf(DataFCMBodyNotification()) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf7f7f7)
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
            Card(modifier = Modifier
                .wrapContentSize(),
                backgroundColor = colordcdcdd,
                shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
            ){
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        contentScale = ContentScale.FillWidth,
                        painter = painterResource(id = R.drawable.icon_fcm),
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
                text = "FCM 현황",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = color000000,
                fontFamily = pretendardvariable,
                fontWeight = FontWeight(weight = 100)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )

            TextField(
                value = getFCM.title,
                onValueChange = {
                    setFCM(getFCM.copy(title = it))
                },
                label = { Text("FCM 제목 입력", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.width(260.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
            TextField(
                value = getFCM.body,
                onValueChange = {
                    setFCM(getFCM.copy(body = it))
                },
                label = { Text("FCM 바디 입력", color = color898989) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0),
                    textColor = color000000
                ),
                modifier = Modifier.width(260.dp)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    FCM.postFCMAlert(context = context, getFCM = getFCM)
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "공지사항 등록",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.doWorker(
                        workManager = workManager,
                        repeatInterval = 15,
                        tag = "TEST",
                        timeMill = TimeUnit.MINUTES
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 시작",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.cancelWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 취소",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = {
                    PeriodicWorker.checkWorker(
                        workManager = workManager,
                        tag = "TEST"
                    )
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "테스트 확인",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = { FCM.getFCMToken(context) },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 토큰 얻기",
                    textAlign = TextAlign.Center,
                    color = colorFFFFFF,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = color20459e),
                onClick = { FCM.postFCMAlertTest(context = context, message = "테스트") },
                modifier = Modifier
                    .width(260.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp)

            ) {
                Text(
                    text = "FCM 테스트",
                    textAlign = TextAlign.Center,
                    color = colorEDE6FD,
                    fontSize = 16.sp,
                    fontFamily = pretendardvariable
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

@Composable
fun MainHeader(image : Int, title : String){
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    )
    Card(modifier = Modifier
        .wrapContentSize(),
        backgroundColor = colordcdcdd,
        shape = RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)
    ){
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
}