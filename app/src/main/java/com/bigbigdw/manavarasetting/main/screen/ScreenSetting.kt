package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import androidx.work.WorkManager
import com.bigbigdw.manavarasetting.R
import com.bigbigdw.manavarasetting.main.model.MainSettingLine
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6

@Composable
fun ScreenMainSetting(
    viewModelMain: ViewModelMain,
    isExpandedScreen: Boolean,
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
) {

    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    var isInit by remember { mutableStateOf(false) }

    if(!isInit){
        viewModelMain.getDataStoreStatus(context = context, workManager = workManager)
        viewModelMain.getDataStoreFCMCount(context = context)
        isInit = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row {
            if(isExpandedScreen){

                val (getMenu, setMenu) = remember { mutableStateOf("세팅바라 현황") }

                val (getDetailPage, setDetailPage) = remember { mutableStateOf(false) }
                val (getDetailPageType, setDetailPageType) = remember { mutableStateOf("") }
                val (getDetailMenu, setDetailMenu) = remember { mutableStateOf("") }

                ScreenTableList(setMenu = setMenu, getMenu = getMenu, onClick = {setDetailPage(false)})

                Spacer(modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = colorf6f6f6))

                if(getDetailPage){
                    ScreenTabletDetail(
                        setDetailPage = setDetailPage,
                        getDetailMenu = getDetailMenu,
                        viewModelMain = viewModelMain,
                        getDetailPageType = getDetailPageType
                    )
                } else {
                    ScreenTablet(
                        title = getMenu,
                        lineTest = lineTest,
                        lineBest = lineBest,
                        lineJson = lineJson,
                        lineTrophy = lineTrophy,
                        viewModelMain = viewModelMain,
                        setDetailPage = setDetailPage,
                        setDetailMenu = setDetailMenu,
                        setDetailPageType = setDetailPageType
                    )
                }

            } else {
                ScreenSettingMobile(
                    viewModelMain = viewModelMain,
                    lineTest = lineTest,
                    lineBest = lineBest,
                    lineJson = lineJson,
                    lineTrophy = lineTrophy
                )
            }
        }
    }
}

@Composable
fun ScreenSettingMobile(
    viewModelMain: ViewModelMain,
    lineTest: List<MainSettingLine>,
    lineBest: List<MainSettingLine>,
    lineJson: List<MainSettingLine>,
    lineTrophy: List<MainSettingLine>,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorf6f6f6)
            .padding(16.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MainHeader(image = R.drawable.ic_launcher, title = "세팅바라 현황")

        ContentsSetting(
            lineTest = lineTest,
            lineBest = lineBest,
            lineJson = lineJson,
            lineTrophy = lineTrophy,
            viewModelMain = viewModelMain
        )
    }
}

