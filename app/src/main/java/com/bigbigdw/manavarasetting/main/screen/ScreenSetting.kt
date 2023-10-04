package com.bigbigdw.manavarasetting.main.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.colorF6F6F6

@Composable
fun ScreenMainSetting(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
) {

    val context = LocalContext.current

    var isInit by remember { mutableStateOf(false) }

    if(!isInit){
        viewModelMain.getDataStoreStatus(context = context)
        viewModelMain.getDataStoreFCMCount()
        isInit = true
    }

    val lineCount = listOf(
        MainSettingLine(title = "베스트 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmBestList.size.toString()),
        MainSettingLine(title = "베스트 금일 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmBestCount.toString()),
        MainSettingLine(title = "JSON 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmJsonList.size.toString()),
        MainSettingLine(title = "JSON 금일 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmJsonCount.toString()),
        MainSettingLine(title = "트로피 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmTrophyList.size.toString()),
        MainSettingLine(title = "트로피 금일 최신화 횟수 : ", value = viewModelMain.state.collectAsState().value.fcmTrophyCount.toString()),
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if(isExpandedScreen){

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }
                val (getDetailMenu, setDetailMenu) = remember { mutableStateOf("") }
                val (getDetailPlatform, setDetailPlatform) = remember { mutableStateOf("") }
                val (getDetailGenre, setDetailGenre) = remember { mutableStateOf("") }
                val (getDetailType, setDetailType) = remember { mutableStateOf("") }

                ScreenTableList(setMenu = setMenu, getMenu = getMenu, onClick = {setDetailPage(false)})

                Spacer(modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = colorF6F6F6))

                if(getDetailPage){
                    ScreenTabletDetail(
                        setDetailPage = setDetailPage,
                        getDetailMenu = getDetailMenu,
                        viewModelMain = viewModelMain,
                        getDetailPlatform = getDetailPlatform,
                        getDetailGenre = getDetailGenre,
                        getDetailType = getDetailType,
                    )
                } else {
                    ScreenTablet(
                        title = getMenu,
                        lineBest = lineBest,
                        lineJson = lineJson,
                        lineTrophy = lineTrophy,
                        viewModelMain = viewModelMain,
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPlatform = setDetailPlatform,
                        setDetailGenre = setDetailGenre,
                        setDetailType = setDetailType,
                        lineCount = lineCount
                    )
                }

            } else {
                ScreenSettingMobile(
                    viewModelMain = viewModelMain,
                    lineBest = lineBest,
                    lineJson = lineJson,
                    lineTrophy = lineTrophy,
                    lineCount = lineCount
                )
            }
        }
    }
}

@Composable
fun ScreenSettingMobile(
    viewModelMain: ViewModelMain,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
    lineCount: List<MainSettingLine>,
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

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ContentsDangerLabs(viewModelMain = viewModelMain)

        ContentsSetting(
            lineBest = lineBest,
            lineJson = lineJson,
            lineTrophy = lineTrophy,
            viewModelMain = viewModelMain,
            lineCount = lineCount
        )
    }
}

