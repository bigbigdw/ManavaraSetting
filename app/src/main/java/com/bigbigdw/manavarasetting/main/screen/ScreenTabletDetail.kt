package com.bigbigdw.manavarasetting.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbigdw.manavarasetting.main.model.BestItemData
import com.bigbigdw.manavarasetting.main.viewModels.ViewModelMain
import com.bigbigdw.manavarasetting.ui.theme.color000000
import com.bigbigdw.manavarasetting.ui.theme.color8e8e8e
import com.bigbigdw.manavarasetting.ui.theme.colorf6f6f6
import com.bigbigdw.manavarasetting.util.WeekKor

@Composable
fun ScreenTabletDetail(
    setDetailPage: (Boolean) -> Unit,
    getDetailMenu: String,
    viewModelMain: ViewModelMain,
    getDetailPageType: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorf6f6f6)
                .verticalScroll(rememberScrollState())
                .semantics { contentDescription = "Overview Screen" },
        ) {

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp)
                    .clickable {
                        setDetailPage(false)
                    },
                text = "< $getDetailMenu",
                fontSize = 24.sp,
                color = color000000,
                fontWeight = FontWeight(weight = 700)
            )

            Spacer(modifier = Modifier.size(16.dp))

            if(getDetailMenu.contains("베스트 리스트")){
                ContentsBestListDetail(viewModelMain = viewModelMain, child = getDetailPageType, type = "BEST")
            } else if(getDetailMenu.contains("투데이 JSON 리스트")){
                ContentsBestListDetail(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType,
                    type = "JSON"
                )
            } else if(getDetailMenu.contains("주간 JSON 리스트")){
                ContentsBestListDetailWeek(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType
                )
            } else if(getDetailMenu.contains("월간 JSON 리스트")){
                ContentsBestListDetailMonth(
                    viewModelMain = viewModelMain,
                    child = getDetailPageType
                )
            }
        }
    }
}

@Composable
fun ContentsBestListDetail(viewModelMain: ViewModelMain, child: String, type: String){

    val bestList: ArrayList<BestItemData> = if(type == "BEST"){
        viewModelMain.getBestList(child = child)
        viewModelMain.state.collectAsState().value.setBestBookList
    } else {
        viewModelMain.getBestJsonList(genre = child)
        viewModelMain.state.collectAsState().value.setBestBookList
    }

    TabletContentWrap(
        radius = 5,
        content = {
            Spacer(modifier = Modifier.size(8.dp))

            bestList.forEachIndexed { index, item ->
                ItemTabletBestList(
                    item = item,
                    isLast = bestList.size - 1 == index
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    )

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailWeek(viewModelMain: ViewModelMain, child: String){

    viewModelMain.getBestJsonWeekList(genre = child)

    val bestWeekList: ArrayList<ArrayList<BestItemData>> = viewModelMain.state.collectAsState().value.bestListWeek
    bestWeekList.forEachIndexed { index, itemArray ->

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(32.dp, 8.dp),
            text = "${WeekKor.get(index)}요일",
            fontSize = 16.sp,
            color = color8e8e8e,
            fontWeight = FontWeight(weight = 700)
        )

        TabletContentWrap(
            radius = 5,
            content = {

                Spacer(modifier = Modifier.size(8.dp))

                if(itemArray.size == 0){
                    Text(
                        text = "없음",
                        color = color000000,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(weight = 500)
                    )
                } else {
                    Row(
                        modifier = Modifier.horizontalScroll(
                            rememberScrollState()
                        )
                    ) {
                        itemArray.forEachIndexed { index, item ->
                            ItemTabletBestListVertical(
                                item = item
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))
            }
        )
    }

    Spacer(modifier = Modifier.size(60.dp))
}

@Composable
fun ContentsBestListDetailMonth(viewModelMain: ViewModelMain, child: String){

    viewModelMain.getBestJsonMonthList(genre = child)

    val bestWeekList: ArrayList<ArrayList<BestItemData>> = viewModelMain.state.collectAsState().value.bestListWeek
    bestWeekList.forEachIndexed { index, itemArray ->

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(32.dp, 8.dp),
            text = "${index + 1}주차",
            fontSize = 16.sp,
            color = color8e8e8e,
            fontWeight = FontWeight(weight = 700)
        )

        TabletContentWrap(
            radius = 5,
            content = {

                Spacer(modifier = Modifier.size(8.dp))

                if(itemArray.size == 0){
                    Text(
                        text = "없음",
                        color = color000000,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(weight = 500)
                    )
                } else {
                    Row(
                        modifier = Modifier.horizontalScroll(
                            rememberScrollState()
                        )
                    ) {
                        itemArray.forEachIndexed { index, item ->
                            ItemTabletBestListVertical(
                                item = item
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))
            }
        )
    }

    Spacer(modifier = Modifier.size(60.dp))
}